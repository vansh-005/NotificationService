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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserEventHandler {

    private final NotificationPreferencesRepository notificationPreferencesRepository;
    private final UsersRepository usersRepository;
    private final ProcessedEventRepository processedEventRepository;


    public void handleUserCreation(UserCreatedEvent userInfo){

        log.info("Creating user");
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
//        processedEventRepository.save(new ProcessedEvent(envelope.eventId()));
    }
    public void handleUserUpdate(UserUpdateEvent userInfo){
        Users user = usersRepository.findByUsername(userInfo.username())
                .orElseThrow(() -> new IllegalStateException(
                        "User not found for update: " + userInfo.username()
                ));


        user.setFirstName(userInfo.firstName());
        user.setLastName(userInfo.lastName());
        user.setEmail(userInfo.email());
        usersRepository.save(user);
    }
}
