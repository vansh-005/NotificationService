package com.example.notificationservice.services;


import com.example.notificationservice.entities.InAppNotifications;
import com.example.notificationservice.entities.Users;
import com.example.notificationservice.repositories.InAppNotificationsRepository;
import com.example.notificationservice.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InAppNotificationService {
    private final InAppNotificationsRepository inAppNotificationsRepository;
    private final UsersRepository usersRepository;

    public Page<InAppNotifications> findAllUnreadNotifications(String username, Pageable pageable) {
        Users user = usersRepository.findByUsername(username).orElseThrow();

        return inAppNotificationsRepository.findAllByUserAndRead(user,false,pageable);
    }

}
