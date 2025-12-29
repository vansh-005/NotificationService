package com.example.notificationservice.event_handlers;


import com.example.notificationservice.DTOs.UpdateNotificationPreferenceEvent;
import com.example.notificationservice.entities.NotificationPreferences;
import com.example.notificationservice.entities.Users;
import com.example.notificationservice.repositories.NotificationPreferencesRepository;
import com.example.notificationservice.repositories.ProcessedEventRepository;
import com.example.notificationservice.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class NotificationPreferenceEventHandler {
    private static final Logger log = LoggerFactory.getLogger(NotificationPreferenceEventHandler.class);
    private final UsersRepository usersRepository;
    private final NotificationPreferencesRepository notificationPreferencesRepository;
    private final ProcessedEventRepository processedEventRepository;

    public void handle(UpdateNotificationPreferenceEvent event) {
        log.info("Received update notification preference ");
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
    }

}
