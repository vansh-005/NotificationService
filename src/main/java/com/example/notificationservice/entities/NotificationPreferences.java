package com.example.notificationservice.entities;


import com.example.notificationservice.enums.NotificationChannels;
import com.example.notificationservice.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class NotificationPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "enabled_notification_types")
    @Column(name = "type")
    private Set<NotificationType> enabledTypes;


    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "notification_preference_channels",
            joinColumns = @JoinColumn(name = "notification_preference_id")
    )
    @Column(name = "channel")
    private List<NotificationChannels> notificationChannel;


}

//    private boolean notifyBlunder;
//    private boolean notifyBrilliant;
//    private boolean notifyMistake;
//    private boolean notifyGameStart;
//    private boolean notifyGameEnd;
//
//    // Add this in CDS as well
//    private boolean notifyInaccuracy;
//    private boolean notifyBest;
//    private boolean notifyExcellent;
//    private boolean notifyGood;