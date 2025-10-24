package com.example.storage;

import com.example.model.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    public static final List<User> users = new ArrayList<>();
    public static final List<Room> rooms = new ArrayList<>();
    public static final List<Device> devices = new ArrayList<>();
    public static final List<AutomationRule> rules = new ArrayList<>();
    public static final List<Event> events = new ArrayList<>();

    // Простой лог для "уведомлений"
    public static final List<String> logs = new ArrayList<>();
}
