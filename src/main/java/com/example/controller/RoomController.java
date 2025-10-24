package com.example.controller;

import com.example.model.Room;
import com.example.model.AutomationRule;
import com.example.model.Device;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @PostMapping("/add")
    public Object add(@RequestParam int id, @RequestParam String name) {
        Room r = new Room(id, name);
        DB.rooms.add(r);
        return r;
    }

    @GetMapping
    public List<Room> all() {
        return DB.rooms;
    }

    @PutMapping("/update")
    public Object update(@RequestParam int id, @RequestParam String name) {
        for (Room r : DB.rooms) {
            if (r.getId() == id) {
                r.setName(name);
                return r;
            }
        }
        return "room not found";
    }

    @DeleteMapping("/delete")
    public Object delete(@RequestParam int id) {
        // Нельзя удалять комнату, если есть устройства или активные правила
        for (Device d : DB.devices) {
            if (d.getRoomId() == id) {
                return "cannot delete: room has devices";
            }
        }
        for (AutomationRule rule : DB.rules) {
            if (rule.isActive() && rule.getRoomId() == id) {
                return "cannot delete: room has active rules";
            }
        }
        for (int i = 0; i < DB.rooms.size(); i++) {
            if (DB.rooms.get(i).getId() == id) {
                DB.rooms.remove(i);
                return "room deleted";
            }
        }
        return "room not found";
    }
}
