package com.example.notificationservice.event_handlers;


import com.example.notificationservice.DTOs.NotificationEnvelope;
import com.example.notificationservice.DTOs.UpdateNotificationPreference;
import com.example.notificationservice.entities.NotificationPreferences;
import com.example.notificationservice.entities.ProcessedEvent;
import com.example.notificationservice.entities.Users;
import com.example.notificationservice.repositories.NotificationPreferencesRepository;
import com.example.notificationservice.repositories.ProcessedEventRepository;
import com.example.notificationservice.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class NotificationPreferenceEventHandler {
    private final UsersRepository usersRepository;
    private final NotificationPreferencesRepository notificationPreferencesRepository;
    private final ProcessedEventRepository processedEventRepository;

    public void handle(NotificationEnvelope envelope) {
        if(processedEventRepository.existsByEventId(envelope.eventId())){
            return;
        }
        if(!(envelope.payload()  instanceof UpdateNotificationPreference event)){
            throw new IllegalArgumentException(
                    "Notification preference does not match notification envelope payload" + envelope.payload().getClass()
            );
        }
        Users user = usersRepository.findByUsername(event.username())
                .orElseThrow(() -> new IllegalStateException(
                        "User not found for notification preference update: " + event.username()
                ));

        NotificationPreferences notificationPreferences = notificationPreferencesRepository.findByUser(user)
                .orElseGet(() -> {
                    NotificationPreferences np = new NotificationPreferences();
                    np.setUser(user);
                    return np;
                });

        notificationPreferences.setNotificationChannel(event.notificationChannels());
        notificationPreferences.setEnabledTypes(event.notificationTypes());
        notificationPreferencesRepository.save(notificationPreferences);
        processedEventRepository.save(new ProcessedEvent(envelope.eventId()));
    }

}
