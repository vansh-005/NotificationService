package com.example.notificationservice.worker_pools;

import com.example.notificationservice.entities.NotificationJobs;
import com.example.notificationservice.entities.SentEmails;
import com.example.notificationservice.entities.SentEmails;
import com.example.notificationservice.enums.NotificationChannels;
import com.example.notificationservice.repositories.NotificationJobsRepository;
import com.example.notificationservice.repositories.SentEmailsRepository;
import com.example.notificationservice.services.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailWorkers {

    private final EmailSender emailSender;
    private final NotificationJobsRepository notificationJobsRepository;
    private final SentEmailsRepository sentEmailsRepository;

    @Qualifier("emailExecutor")
    private final Executor emailExecutor;

    private final String workerId = UUID.randomUUID().toString();

    // rate limit: max concurrent email sends
    private final Semaphore emailRateLimiter = new Semaphore(5, true);

    @Value("${worker-pool.email.max-retries}")
    private int maxRetries;

    @Value("${worker-pool.email.batch-size}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${worker-pool.email.polling-interval}")
    public void pollEmailJob() {
        List<NotificationJobs> jobs =
                notificationJobsRepository.claimNextJobs(
                        NotificationChannels.EMAIL.name(),
                        batchSize,
                        workerId
                );

        for (NotificationJobs job : jobs) {
            emailExecutor.execute(() -> processEmail(job));
        }
    }

    private void processEmail(NotificationJobs job) {
        try {
            // ---- Idempotency guard (DB-enforced) ----
            try {
                sentEmailsRepository.save(new SentEmails(job.getId()));
            } catch (DataIntegrityViolationException e) {
                // Email already sent earlier → idempotent success
                log.info("Email already sent for jobId={}, marking completed", job.getId());
                notificationJobsRepository.markCompleted(job.getId());
                return;
            }

            // ---- Render email (placeholder for template engine) ----
            String subject = "Dummy_subject";
            String body = "Dummy_body";
            String userEmail = job.getUser().getEmail();


            try {
                emailRateLimiter.acquire();
                try {
                    emailSender.send(userEmail, subject, body);
                    notificationJobsRepository.markCompleted(job.getId());
                } finally {
                    emailRateLimiter.release();
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                handleFailure(job, ie);
            }
        }
        catch (Exception ex) {
            log.warn(
                    "Sending email failed jobId={}, retry={}",
                    job.getId(),
                    job.getRetryCount(),
                    ex
            );
            handleFailure(job, ex);
        }
    }

    private void handleFailure(NotificationJobs job, Exception ex) {
        int nextRetry = job.getRetryCount() + 1;

        if (nextRetry >= maxRetries) {
            notificationJobsRepository.markFailed(job.getId());
        } else {
            notificationJobsRepository.reschedule(
                    job.getId(),
                    nextRetry,
                    computeNextAttempt(nextRetry)
            );
        }
    }

    private Instant computeNextAttempt(int retryCount) {
        long delaySeconds = Math.min(60, (long) Math.pow(2, retryCount));
        return Instant.now().plusSeconds(delaySeconds);
    }
}
