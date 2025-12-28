package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.SentEmails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentEmailsRepository extends JpaRepository<SentEmails, Long> {
}