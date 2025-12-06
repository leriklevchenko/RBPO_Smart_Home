package com.example.controller;

import com.example.model.AutomationRule;
import com.example.model.Room;
import com.example.repository.AutomationRuleRepository;
import com.example.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class AutomationRuleController {

    private final AutomationRuleRepository automationRuleRepository;
    private final RoomRepository roomRepository;

    @PostMapping("/add")
    public Object add(@RequestParam int id,
                      @RequestParam int roomId,
                      @RequestParam String eventType,
                      @RequestParam String action,
                      @RequestParam(defaultValue = "true") boolean active) {
        if (automationRuleRepository.existsById(id)) {
            return "rule with id " + id + " already exists";
        }
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return "room not found";
        }
        AutomationRule r = new AutomationRule();
        r.setId(id);
        r.setRoom(room);
        r.setEventType(eventType);
        r.setAction(action);
        r.setActive(active);
        return automationRuleRepository.save(r);
    }

    @GetMapping
    public List<AutomationRule> all() {
        return automationRuleRepository.findAll();
    }

    @PutMapping("/update")
    public Object update(@RequestParam int id,
                         @RequestParam(required = false) Integer roomId,
                         @RequestParam(required = false) String eventType,
                         @RequestParam(required = false) String action,
                         @RequestParam(required = false) Boolean active) {
        AutomationRule r = automationRuleRepository.findById(id).orElse(null);
        if (r == null) {
            return "rule not found";
        }
        if (roomId != null) {
            Room room = roomRepository.findById(roomId).orElse(null);
            if (room == null) {
                return "room not found";
            }
            r.setRoom(room);
        }
        if (eventType != null) {
            r.setEventType(eventType);
        }
        if (action != null) {
            r.setAction(action);
        }
        if (active != null) {
            r.setActive(active);
        }
        return automationRuleRepository.save(r);
    }

    @DeleteMapping("/delete")
    public Object delete(@RequestParam int id) {
        if (!automationRuleRepository.existsById(id)) {
            return "rule not found";
        }
        automationRuleRepository.deleteById(id);
        return "rule deleted";
    }
}
