package com.example.controller;

import com.example.model.Device;
import com.example.model.Room;
import com.example.repository.DeviceRepository;
import com.example.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;

    @PostMapping("/add")
    public Object add(@RequestParam int id,
                      @RequestParam String name,
                      @RequestParam int roomId,
                      @RequestParam String type,
                      @RequestParam(defaultValue = "false") boolean on) {
        if (deviceRepository.existsById(id)) {
            return "device with id " + id + " already exists";
        }
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return "room not found";
        }

        Device d = new Device();
        d.setId(id);
        d.setName(name);
        d.setRoom(room);
        d.setType(type);
        d.setOn(on);
        return deviceRepository.save(d);
    }

    @GetMapping
    public List<Device> all() {
        return deviceRepository.findAll();
    }

    @PutMapping("/update")
    public Object update(@RequestParam int id,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) Integer roomId,
                         @RequestParam(required = false) String type,
                         @RequestParam(required = false) Boolean on) {
        Device device = deviceRepository.findById(id).orElse(null);
        if (device == null) {
            return "device not found";
        }
        if (name != null) {
            device.setName(name);
        }
        if (roomId != null) {
            Room newRoom = roomRepository.findById(roomId).orElse(null);
            if (newRoom == null) {
                return "room not found";
            }
            device.setRoom(newRoom);
        }
        if (type != null) {
            device.setType(type);
        }
        if (on != null) {
            device.setOn(on);
        }
        return deviceRepository.save(device);
    }

    @DeleteMapping("/delete")
    public Object delete(@RequestParam int id) {
        if (!deviceRepository.existsById(id)) {
            return "device not found";
        }
        deviceRepository.deleteById(id);
        return "device deleted";
    }

    // Business operation: toggle device state on/off
    @PostMapping("/toggle")
    public Object toggle(@RequestParam int id) {
        Device device = deviceRepository.findById(id).orElse(null);
        if (device == null) {
            return "device not found";
        }
        device.setOn(!device.isOn());
        return deviceRepository.save(device);
    }
}
