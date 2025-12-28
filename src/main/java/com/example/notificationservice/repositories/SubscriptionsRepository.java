package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.Subscriptions;
import com.example.notificationservice.entities.Users;
import com.example.notificationservice.enums.TargetType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Long> {
    public Optional<Subscriptions> findByUserAndTargetTypeAndTargetId(Users user, TargetType targetType, String targetId);
    public boolean existsByUserAndTargetTypeAndTargetId(Users user, TargetType targetType, String targetId);

    @Query("""
        select distinct s.user
        from Subscriptions s
        where s.targetType = :targetType
          and s.targetId = :targetId
    """)
    Set<Users> findDistinctUsersByTarget(
            TargetType targetType,
            String targetId
    );
}