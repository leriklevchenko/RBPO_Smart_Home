package com.example.controller;

import com.example.model.AutomationRule;
import com.example.model.Device;
import com.example.model.Event;
import com.example.model.LogEntry;
import com.example.repository.AutomationRuleRepository;
import com.example.repository.DeviceRepository;
import com.example.repository.EventRepository;
import com.example.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final DeviceRepository deviceRepository;
    private final AutomationRuleRepository automationRuleRepository;
    private final LogEntryRepository logEntryRepository;

    @PostMapping("/add")
    @Transactional
    public Object add(@RequestParam int id,
                      @RequestParam int deviceId,
                      @RequestParam String type,
                      @RequestParam(required = false) String data) {

        if (eventRepository.existsById(id)) {
            return "event with id " + id + " already exists";
        }

        Device found = deviceRepository.findById(deviceId).orElse(null);
        if (found == null) {
            return "device not found";
        }

        Event e = new Event();
        e.setId(id);
        e.setDevice(found);
        e.setType(type);
        e.setData(data);
        eventRepository.save(e);

        // Simple rule processing
        int roomId = found.getRoom().getId();
        List<AutomationRule> rules = automationRuleRepository
                .findByRoomIdAndEventTypeAndActiveTrue(roomId, type);

        for (AutomationRule r : rules) {

            if ("turn_on_light".equals(r.getAction())) {
                List<Device> lights = deviceRepository.findByRoomIdAndType(roomId, "light");
                for (Device d : lights) {
                    d.setOn(true);
                }
                deviceRepository.saveAll(lights);

                LogEntry log = new LogEntry();
                log.setMessage("rule " + r.getId() + ": turn_on_light in room " + roomId);
                logEntryRepository.save(log);
            }

            if ("notify_user".equals(r.getAction())) {
                LogEntry log = new LogEntry();
                log.setMessage("rule " + r.getId() + ": notify_user about event " + type + " in room " + roomId);
                logEntryRepository.save(log);
            }
        }

        return e;
    }

    @GetMapping
    public List<Event> all() {
        return eventRepository.findAll();
    }

    @GetMapping("/logs")
    public List<String> logs() {
        List<LogEntry> entries = logEntryRepository.findAll();
        List<String> result = new ArrayList<>();
        for (LogEntry entry : entries) {
            result.add(entry.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    public Object update(@RequestParam int id,
                         @RequestParam(required = false) Integer deviceId,
                         @RequestParam(required = false) String type,
                         @RequestParam(required = false) String data) {
        Event e = eventRepository.findById(id).orElse(null);
        if (e == null) {
            return "event not found";
        }
        if (deviceId != null) {
            Device device = deviceRepository.findById(deviceId).orElse(null);
            if (device == null) {
                return "device not found";
            }
            e.setDevice(device);
        }
        if (type != null) {
            e.setType(type);
        }
        if (data != null) {
            e.setData(data);
        }
        return eventRepository.save(e);
    }

    @DeleteMapping("/delete")
    public Object delete(@RequestParam int id) {
        if (!eventRepository.existsById(id)) {
            return "event not found";
        }
        eventRepository.deleteById(id);
        return "event deleted";
    }
}
