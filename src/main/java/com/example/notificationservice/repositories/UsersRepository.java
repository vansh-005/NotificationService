package com.example.notificationservice.repositories;

import com.example.notificationservice.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
    public Optional<Users> findByUsername(String username);
}