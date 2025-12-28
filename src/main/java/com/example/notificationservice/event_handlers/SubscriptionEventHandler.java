package com.example.notificationservice.event_handlers;

import com.example.notificationservice.DTOs.NotificationEnvelope;
import com.example.notificationservice.DTOs.SubscriptionChangedEvent;
import com.example.notificationservice.entities.ProcessedEvent;
import com.example.notificationservice.entities.Subscriptions;
import com.example.notificationservice.entities.Users;
import com.example.notificationservice.repositories.ProcessedEventRepository;
import com.example.notificationservice.repositories.SubscriptionsRepository;
import com.example.notificationservice.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Transactional
public class SubscriptionEventHandler {

    private final UsersRepository usersRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final ProcessedEventRepository processedEventRepository;

    public void handle(NotificationEnvelope envelope){

        if(processedEventRepository.existsByEventId(envelope.eventId())){
            return;
        }
        if(!(envelope.payload() instanceof SubscriptionChangedEvent subscription)){
            throw new IllegalArgumentException(
                    "Invalid payload for SUBSCRIPTION_CHANGING" + envelope.payload().getClass()
            );
        }
        Users user = usersRepository.findByUsername(subscription.username()).orElseThrow(
                () -> new IllegalStateException(
                        "User not found for subscription change: " + subscription.username()
                ));
        switch (subscription.action()){
            case ADD -> {
                boolean exists = subscriptionsRepository
                        .existsByUserAndTargetTypeAndTargetId(
                                user,
                                subscription.targetType(),
                                subscription.targetId()
                        );
                Subscriptions newSubscription = new Subscriptions();
                newSubscription.setUser(user);
                newSubscription.setTargetType(subscription.targetType());
                newSubscription.setTargetId(subscription.targetId());
                subscriptionsRepository.save(newSubscription);
            }
            case REMOVE -> {
                subscriptionsRepository.findByUserAndTargetTypeAndTargetId(user, subscription.targetType(), subscription.targetId()).ifPresent(subscriptionsRepository::delete);
            }
        }
        processedEventRepository.save(new ProcessedEvent(envelope.eventId()));
    }

}
