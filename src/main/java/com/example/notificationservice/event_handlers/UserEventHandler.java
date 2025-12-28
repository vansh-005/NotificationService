package com.example.notificationservice.event_handlers;


import com.example.notificationservice.DTOs.NotificationEnvelope;
import com.example.notificationservice.DTOs.UserCreatedEvent;
import com.example.notificationservice.DTOs.UserUpdateEvent;
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
@RequiredArgsConstructor
@Transactional
public class UserEventHandler {

    private final NotificationPreferencesRepository notificationPreferencesRepository;
    private final UsersRepository usersRepository;
    private final ProcessedEventRepository processedEventRepository;


    public void handleUserCreation(NotificationEnvelope envelope){
        if(!(envelope.payload() instanceof UserCreatedEvent userInfo)){
            throw new IllegalArgumentException(
                    "Invalid payload for USER_CREATED" + envelope.payload().getClass()
            );
        }
        if(processedEventRepository.existsByEventId(envelope.eventId())){
            return;
        }
        Users user = new Users();
        user.setUsername(userInfo.username());
        user.setFirstName(userInfo.firstName());
        user.setLastName(userInfo.lastName());
        user.setEmail(userInfo.email());

        NotificationPreferences notificationPreferences = new NotificationPreferences();
        notificationPreferences.setUser(user);
        notificationPreferences.setEnabledTypes(userInfo.enabledTypes());
        notificationPreferences.setNotificationChannel(userInfo.channels());

        user.setNotificationPreferences(notificationPreferences);
//        notificationPreferencesRepository.save(notificationPreferences);
        // Since cascading enabled so saving user automatically saves notification preferences
        usersRepository.save(user);
        processedEventRepository.save(new ProcessedEvent(envelope.eventId()));
    }
    public void handleUserUpdate(NotificationEnvelope envelope){
        if(!(envelope.payload() instanceof UserUpdateEvent userInfo)){
            throw new IllegalArgumentException(
                    "Invalid payload for USER_UPDATE" + envelope.payload().getClass()
            );
        }

        if (processedEventRepository.existsByEventId(envelope.eventId())) {
            return;
        }
        Users user = usersRepository.findByUsername(userInfo.username())
                .orElseThrow(() -> new IllegalStateException(
                        "User not found for update: " + userInfo.username()
                ));


        user.setFirstName(userInfo.firstName());
        user.setLastName(userInfo.lastName());
        user.setEmail(userInfo.email());
        usersRepository.save(user);

        processedEventRepository.save(new ProcessedEvent(envelope.eventId()));
    }
}
