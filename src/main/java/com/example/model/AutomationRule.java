package com.example.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutomationRule {
    private int id;
    private int roomId;        // для какой комнаты действует правило
    private String eventType;  // на какое событие реагируем (например, "motion")
    private String action;     // что делаем: "turn_on_light" или "notify_user"
    private boolean active;    // активно ли правило
}
