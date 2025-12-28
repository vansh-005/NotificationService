package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.NotificationJobs;
import com.example.notificationservice.entities.Users;
import com.example.notificationservice.enums.NotificationChannels;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface NotificationJobsRepository extends JpaRepository<NotificationJobs, Long> {


    @Modifying
    @Transactional
    @Query(value = """
   UPDATE notification_jobs
   SET
     status = 'PROCESSING',
     locked_at = now(),
     locked_by = :workerId
   WHERE id IN (
     SELECT id FROM notification_jobs
     WHERE status = 'PENDING'
         AND (next_attempt_at IS NULL OR next_attempt_at <= now())
       AND channel = :channel
       AND (locked_at IS NULL OR locked_at < now() - interval '2 minutes')
     ORDER BY created_at
     LIMIT :batch
     FOR UPDATE SKIP LOCKED
   )
   RETURNING *;
   
""",nativeQuery = true)
    List<NotificationJobs> claimNextJobs(
            @Param("channel") String channel,
            @Param("batch") int batch,
            @Param("workerId") String workerId
    );

    @Modifying
    @Transactional
    @Query("""
    UPDATE NotificationJobs j
    SET j.status = 'SENT',
        j.updatedAt = CURRENT_TIMESTAMP,
        j.lockedAt = NULL,
        j.lockedBy = NULL
    WHERE j.id = :id
""")
    public void markCompleted(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("""
    UPDATE NotificationJobs j
    SET j.status = 'PENDING',
        j.retryCount = :retryCount,
        j.nextAttemptAt = :nextAttemptAt,
        j.updatedAt = CURRENT_TIMESTAMP,
        j.lockedAt = NULL,
        j.lockedBy = NULL
    WHERE j.id = :id
""")
    void reschedule(
            @Param("id") Long id,
            @Param("retryCount") int retryCount,
            @Param("nextAttemptAt") Instant nextAttemptAt
    );

    @Modifying
    @Transactional
    @Query("""
    UPDATE NotificationJobs j
    SET j.status = 'FAILED',
        j.updatedAt = CURRENT_TIMESTAMP,
        j.lockedAt = NULL,
        j.lockedBy = NULL
    WHERE j.id = :id
""")
    void markFailed(@Param("id") Long id);


}