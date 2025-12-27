package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.NotificationJobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJobsRepository extends JpaRepository<NotificationJobs, Long> {
}