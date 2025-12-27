package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.NotificationPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationPreferencesRepository extends JpaRepository<NotificationPreferences, Long> {
}