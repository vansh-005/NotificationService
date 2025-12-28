package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.InAppNotifications;
import com.example.notificationservice.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InAppNotificationsRepository extends JpaRepository<InAppNotifications, Long> {
    Page<InAppNotifications> findAllByUserAndRead(Users user, boolean read, Pageable pageable);
}