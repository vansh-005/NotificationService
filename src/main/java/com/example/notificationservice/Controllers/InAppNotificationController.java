package com.example.notificationservice.Controllers;

import com.example.notificationservice.entities.InAppNotifications;
import com.example.notificationservice.services.InAppNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification/inapp")
public class InAppNotificationController {

    private final InAppNotificationService inAppNotificationService;
    // TODO: Add a seen flag later
    @GetMapping("/{username}")
    public ResponseEntity<Page<InAppNotifications>> getInAppNotification(
            @RequestParam String username,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(inAppNotificationService.findAllUnreadNotifications(username,pageable));
    }
}
