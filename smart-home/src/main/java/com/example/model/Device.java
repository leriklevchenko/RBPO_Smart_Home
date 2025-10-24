package com.example.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    private int id;
    private String name;
    private int roomId;    // в какой комнате
    private String type;   // пример: "light", "sensor"
    private boolean on;    // включено/выключено
}
