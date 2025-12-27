package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.Subscriptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Long> {
}