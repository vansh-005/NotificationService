package com.example.notificationservice.messages;

import com.example.notificationservice.DTOs.*;
import com.example.notificationservice.entities.ProcessedEvent;
import com.example.notificationservice.event_handlers.NotificationEventHandler;
import com.example.notificationservice.event_handlers.NotificationPreferenceEventHandler;
import com.example.notificationservice.event_handlers.SubscriptionEventHandler;
import com.example.notificationservice.event_handlers.UserEventHandler;
import com.example.notificationservice.repositories.ProcessedEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j

public class NotificationEventRouter {
    private final UserEventHandler userEventHandler;
    private final SubscriptionEventHandler subscriptionEventHandler;
    private final NotificationPreferenceEventHandler notificationPreferenceEventHandler;
    private final NotificationEventHandler notificationEventHandler;

    private final ObjectMapper objectMapper;
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    public void route(NotificationEnvelope envelope) {
        try {
            processedEventRepository.save(new ProcessedEvent(envelope.eventId()));
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate event {} ignored", envelope.eventId());
            return;
        }
        switch(envelope.eventType()){
            case "USER_CREATED" -> {
                try {
                    UserCreatedEvent event = objectMapper.readValue(envelope.payload(), UserCreatedEvent.class);
                    userEventHandler.handleUserCreation(event);
                }
                catch (IOException ex) {
                    log.error("Could not parse event {}", envelope.eventId());
                }
            }
            case "USER_UPDATED" -> {
                try {
                    UserUpdateEvent event = objectMapper.readValue(envelope.payload(), UserUpdateEvent.class);
                    userEventHandler.handleUserUpdate(event);
                }
                catch (IOException ex){
                    log.error("Could not parse event {}", envelope.eventId());
                }
            }
            case "NOTIFICATION_PREFERENCE_UPDATED" -> {
                try{

//                    log.info("Trying to update notification preference");
//                    log.info(envelope.payload());
                    UpdateNotificationPreferenceEvent event = objectMapper.readValue(envelope.payload(), UpdateNotificationPreferenceEvent.class);
//                    log.info(event.toString());
                    notificationPreferenceEventHandler.handle(event);
                }
                catch (IOException ex){
                    log.error("Could not parse event {}", envelope.eventId());
                }
            }
            case "SUBSCRIPTION_CHANGED" -> {
                try{
                    SubscriptionChangedEvent event = objectMapper.readValue(envelope.payload(), SubscriptionChangedEvent.class);
                    subscriptionEventHandler.handle(event);
                }
                catch (IOException ex){
                    log.error("Could not parse event {}", envelope.eventId());
                }
            }
            case "NOTIFICATION_EVENT" -> {
                try{
                NotificationEvent event = objectMapper.readValue(envelope.payload(), NotificationEvent.class);
                notificationEventHandler.handle(event);
                }
                catch (IOException ex){
                    log.error("Could not parse event {}", envelope.eventId());
                }
            }
            default -> {
                log.error("Unsupported event type {}", envelope.eventType());
                return;
            }
        }

    }
}
