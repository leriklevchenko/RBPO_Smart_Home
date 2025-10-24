package com.example.controller;

import com.example.model.Device;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @PostMapping("/add")
    public Object add(@RequestParam int id, @RequestParam String name, @RequestParam int roomId, @RequestParam String type, @RequestParam(defaultValue = "false") boolean on) {
        Device d = new Device(id, name, roomId, type, on);
        DB.devices.add(d);
        return d;
    }

    @GetMapping
    public List<Device> all() {
        return DB.devices;
    }

    @PutMapping("/update")
    public Object update(@RequestParam int id, @RequestParam(required = false) String name, @RequestParam(required = false) Integer roomId, @RequestParam(required = false) String type, @RequestParam(required = false) Boolean on) {
        for (Device d : DB.devices) {
            if (d.getId() == id) {
                if (name != null) d.setName(name);
                if (roomId != null) d.setRoomId(roomId);
                if (type != null) d.setType(type);
                if (on != null) d.setOn(on);
                return d;
            }
        }
        return "device not found";
    }

    @DeleteMapping("/delete")
    public Object delete(@RequestParam int id) {
        for (int i = 0; i < DB.devices.size(); i++) {
            if (DB.devices.get(i).getId() == id) {
                DB.devices.remove(i);
                return "device deleted";
            }
        }
        return "device not found";
    }
}
