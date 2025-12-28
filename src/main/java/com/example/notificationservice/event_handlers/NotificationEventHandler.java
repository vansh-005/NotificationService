package com.example.notificationservice.event_handlers;

import com.example.notificationservice.DTOs.NotificationEnvelope;
import com.example.notificationservice.DTOs.NotificationEvent;
import com.example.notificationservice.DTOs.TargetRef;
import com.example.notificationservice.entities.NotificationJobs;
import com.example.notificationservice.entities.NotificationPreferences;
import com.example.notificationservice.entities.ProcessedEvent;
import com.example.notificationservice.entities.Users;
import com.example.notificationservice.enums.NotificationChannels;
import com.example.notificationservice.enums.NotificationType;
import com.example.notificationservice.enums.TargetType;
import com.example.notificationservice.repositories.NotificationJobsRepository;
import com.example.notificationservice.repositories.NotificationPreferencesRepository;
import com.example.notificationservice.repositories.ProcessedEventRepository;
import com.example.notificationservice.repositories.SubscriptionsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;
//import tools.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class NotificationEventHandler {

    private final ProcessedEventRepository processedEventRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final NotificationPreferencesRepository notificationPreferencesRepository;
    private final NotificationJobsRepository notificationJobsRepository;
//    private final ObjectMapper objectMapper;

    public void handle(NotificationEnvelope envelope){
        if(processedEventRepository.existsByEventId(envelope.eventId())){
            return;
        }

        if(!(envelope.payload() instanceof NotificationEvent event)){
            throw new IllegalArgumentException(
                    "Invalid payload for Notification event: " + envelope.payload().getClass()
            );
        }
        String payloadJson = envelope.payload().toString();
        Set<Users> users = new HashSet<>();

        for(TargetRef targetRef: event.targets()){
            users.addAll(
                    subscriptionsRepository.findDistinctUsersByTarget(targetRef.targetType(),targetRef.targetId())
            );
        }

        // Understand this part properly
        Map<Long, NotificationPreferences> preferencesMap =
        notificationPreferencesRepository.findByUsers(users)
                        .stream()
                                .collect(Collectors.toMap(
                                        np -> np.getUser().getId(),
                                        np -> np
                                ));

        List<NotificationJobs> jobs = new ArrayList<>();
        Set<String> jobKeys = new HashSet<>();

        for(Users user: users){
            NotificationPreferences pref = preferencesMap.get(user.getId());
            if(pref == null){continue;}
            if (!pref.getEnabledTypes().contains(event.notificationType())) continue;
            for(NotificationChannels channel: pref.getNotificationChannel()){

                String key = user.getId() + ":" + channel;
                if (!jobKeys.add(key)) continue;

                NotificationJobs job = new NotificationJobs();
                job.setUser(user);
                job.setNotificationType(event.notificationType());
                job.setNotificationChannel(channel);
                job.setPayload(payloadJson);

                jobs.add(job);
            }
        }
        notificationJobsRepository.saveAll(jobs);
        processedEventRepository.save(new ProcessedEvent(envelope.eventId()));
    }

}
