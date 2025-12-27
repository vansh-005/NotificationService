package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.InAppNotifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InAppNotificationsRepository extends JpaRepository<InAppNotifications, Long> {
}