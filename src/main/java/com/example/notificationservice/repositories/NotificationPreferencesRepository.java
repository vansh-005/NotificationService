package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.NotificationPreferences;
import com.example.notificationservice.entities.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NotificationPreferencesRepository extends JpaRepository<NotificationPreferences, Long> {
    public Optional<NotificationPreferences> findByUsers(Users user);
    @Query("""
        select np from NotificationPreferences np
        where np.user in :users
""")
    public List<NotificationPreferences> findByUsers(Set<Users> users);
}