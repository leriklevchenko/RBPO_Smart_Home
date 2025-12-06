package com.example.controller;

import com.example.model.Room;
import com.example.model.Device;
import com.example.model.Event;
import com.example.repository.RoomRepository;
import com.example.repository.DeviceRepository;
import com.example.repository.AutomationRuleRepository;
import com.example.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;
    private final AutomationRuleRepository automationRuleRepository;
    private final EventRepository eventRepository;

    @PostMapping("/add")
    public Object add(@RequestParam int id, @RequestParam String name) {
        if (roomRepository.existsById(id)) {
            return "room with id " + id + " already exists";
        }
        Room r = new Room();
        r.setId(id);
        r.setName(name);
        return roomRepository.save(r);
    }

    @GetMapping
    public List<Room> all() {
        return roomRepository.findAll();
    }

    @PutMapping("/update")
    public Object update(@RequestParam int id, @RequestParam String name) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            return "room not found";
        }
        room.setName(name);
        return roomRepository.save(room);
    }

    @DeleteMapping("/delete")
    @Transactional
    public Object delete(@RequestParam int id) {
        if (!roomRepository.existsById(id)) {
            return "room not found";
        }
        if (deviceRepository.existsByRoomId(id)) {
            return "cannot delete: room has devices";
        }
        if (automationRuleRepository.existsByRoomIdAndActiveTrue(id)) {
            return "cannot delete: room has active rules";
        }
        roomRepository.deleteById(id);
        return "room deleted";
    }

    // Business operation: turn off all devices in the room
    @PostMapping("/{id}/turnOffAllDevices")
    @Transactional
    public Object turnOffAllDevices(@PathVariable int id) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            return "room not found";
        }
        List<Device> devices = deviceRepository.findByRoomId(id);
        for (Device d : devices) {
            d.setOn(false);
        }
        deviceRepository.saveAll(devices);
        return "all devices in room " + id + " were turned off";
    }

    // Business operation: aggregated status of room (devices + last events)
    @GetMapping("/{id}/status")
    public Object roomStatus(@PathVariable int id) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            return "room not found";
        }
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("room", room);
        List<Device> devices = deviceRepository.findByRoomId(id);
        dto.put("devices", devices);

        Map<Integer, Event> lastEvents = new HashMap<>();
        for (Device d : devices) {
            List<Event> last = eventRepository.findTop1ByDeviceIdOrderByIdDesc(d.getId());
            if (!last.isEmpty()) {
                lastEvents.put(d.getId(), last.get(0));
            }
        }
        dto.put("lastEvents", lastEvents);
        return dto;
    }
}
