package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "automation_rules")
@Data
@NoArgsConstructor
public class AutomationRule {

    @Id
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;        // room where rule applies

    @Column(nullable = false)
    private String eventType;  // event type to react on (e.g. "motion")

    @Column(nullable = false)
    private String action;     // "turn_on_light" or "notify_user"

    @Column(nullable = false)
    private boolean active;    // is rule active
}