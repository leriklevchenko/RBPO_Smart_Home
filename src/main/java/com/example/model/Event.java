package com.example.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private int id;
    private int deviceId;
    private String type;   // тип события, например "motion", "open", "temp"
    private String data;   // произвольные данные
}
