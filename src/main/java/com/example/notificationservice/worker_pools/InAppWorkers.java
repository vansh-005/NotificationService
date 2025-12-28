package com.example.notificationservice.worker_pools;

import com.example.notificationservice.entities.InAppNotifications;
import com.example.notificationservice.entities.NotificationJobs;
import com.example.notificationservice.enums.NotificationChannels;
import com.example.notificationservice.repositories.InAppNotificationsRepository;
import com.example.notificationservice.repositories.NotificationJobsRepository;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class InAppWorkers {
    private final NotificationJobsRepository notificationJobsRepository;
    private final String workerId = UUID.randomUUID().toString();
    private final InAppNotificationsRepository inAppNotificationsRepository;

    @Value("${worker-pool.in-app.max-retries}")
    private int maxRetries;

    @Value("${worker-pool.in-app.batch-size}")
    private int batchSize ;

    @Qualifier("inAppExecutor")
    private final Executor inAppExecutor;

    @Scheduled(fixedDelayString = "${worker-pool.in-app.polling-interval}")
    private void pollInApJobs(){
        List<NotificationJobs> jobs =  notificationJobsRepository
                .claimNextJobs(NotificationChannels.IN_APP.name(),batchSize,workerId);
        for(NotificationJobs job : jobs){
            inAppExecutor.execute(() -> processInApp(job));
        }
    }
    private void processInApp(NotificationJobs job){
        try{
            // TODO: Render notification from payload
            String title = "dummy_title";
            String body = "dummy_body";

            InAppNotifications inAppNotifications = new InAppNotifications();
            inAppNotifications.setUser(job.getUser());
            inAppNotifications.setTitle(title);
            inAppNotifications.setBody(body);
            inAppNotifications.setJobId(job.getId());
            inAppNotifications.setRead(false);



            inAppNotificationsRepository.save(inAppNotifications);
            notificationJobsRepository.markCompleted(job.getId());

        }
        catch (DataIntegrityViolationException e) {
            // Idempotent success: notification already created
            log.info("Duplicate in-app notification for jobId={}, marking completed", job.getId());
            notificationJobsRepository.markCompleted(job.getId());
        }
        catch (Exception ex){
            log.warn(
                    "InApp notification failed jobId={}, retry={}",
                    job.getId(),
                    job.getRetryCount(),
                    ex
            );

            handleFailure(job,ex);
        }
    }
    private void handleFailure(NotificationJobs job, Exception ex){
        int nextRetry = job.getRetryCount() + 1;

        if(nextRetry >= maxRetries){
            notificationJobsRepository.markFailed(job.getId());
        }else{
            notificationJobsRepository.reschedule(
                    job.getId(), nextRetry, computeNextAttempt(nextRetry)
            );
        }
    }

    private Instant computeNextAttempt(int retryCount) {
        long delaySeconds = Math.min(60, (long) Math.pow(2, retryCount));
        return Instant.now().plusSeconds(delaySeconds);
    }

}
