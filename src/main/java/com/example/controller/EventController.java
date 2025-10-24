package com.example.controller;

import com.example.model.AutomationRule;
import com.example.model.Device;
import com.example.model.Event;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @PostMapping("/add")
    public Object add(@RequestParam int id,
                      @RequestParam int deviceId,
                      @RequestParam String type,
                      @RequestParam(required = false) String data) {
        // Находим устройство
        Device found = null;
        for (Device d : DB.devices) {
            if (d.getId() == deviceId) {
                found = d;
                break;
            }
        }
        if (found == null) return "device not found";

        Event e = new Event(id, deviceId, type, data);
        DB.events.add(e);

        // Простейшая обработка правил
        int roomId = found.getRoomId();
        for (AutomationRule r : DB.rules) {
            if (r.isActive()
                    && r.getRoomId() == roomId
                    && r.getEventType().equals(type)) {

                if (r.getAction().equals("turn_on_light")) {
                    for (Device d : DB.devices) {
                        if (d.getRoomId() == roomId && "light".equals(d.getType())) {
                            d.setOn(true);
                        }
                    }
                    DB.logs.add("rule " + r.getId() + ": turn_on_light in room " + roomId);
                }

                if (r.getAction().equals("notify_user")) {
                    DB.logs.add("rule " + r.getId() + ": notify_user about event " + type + " in room " + roomId);
                }
            }
        }

        return e;
    }

    @GetMapping
    public List<Event> all() {
        return DB.events;
    }

    @GetMapping("/logs")
    public List<String> logs() {
        return DB.logs;
    }

    @PutMapping("/update")
    public Object update(@RequestParam int id,
                         @RequestParam(required = false) Integer deviceId,
                         @RequestParam(required = false) String type,
                         @RequestParam(required = false) String data) {
        for (Event e : DB.events) {
            if (e.getId() == id) {
                if (deviceId != null) e.setDeviceId(deviceId);
                if (type != null) e.setType(type);
                if (data != null) e.setData(data);
                return e;
            }
        }
        return "event not found";
    }

    @DeleteMapping("/delete")
    public Object delete(@RequestParam int id) {
        for (int i = 0; i < DB.events.size(); i++) {
            if (DB.events.get(i).getId() == id) {
                DB.events.remove(i);
                return "event deleted";
            }
        }
        return "event not found";
    }
}
