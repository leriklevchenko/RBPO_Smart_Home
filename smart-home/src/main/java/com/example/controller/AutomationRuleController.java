package com.example.controller;

import com.example.model.AutomationRule;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class AutomationRuleController {

    @PostMapping("/add")
    public Object add(@RequestParam int id,
                      @RequestParam int roomId,
                      @RequestParam String eventType,
                      @RequestParam String action,
                      @RequestParam(defaultValue = "true") boolean active) {
        AutomationRule r = new AutomationRule(id, roomId, eventType, action, active);
        DB.rules.add(r);
        return r;
    }

    @GetMapping
    public List<AutomationRule> all() {
        return DB.rules;
    }

    @PutMapping("/update")
    public Object update(@RequestParam int id,
                         @RequestParam(required = false) Integer roomId,
                         @RequestParam(required = false) String eventType,
                         @RequestParam(required = false) String action,
                         @RequestParam(required = false) Boolean active) {
        for (AutomationRule r : DB.rules) {
            if (r.getId() == id) {
                if (roomId != null) r.setRoomId(roomId);
                if (eventType != null) r.setEventType(eventType);
                if (action != null) r.setAction(action);
                if (active != null) r.setActive(active);
                return r;
            }
        }
        return "rule not found";
    }

    @DeleteMapping("/delete")
    public Object delete(@RequestParam int id) {
        for (int i = 0; i < DB.rules.size(); i++) {
            if (DB.rules.get(i).getId() == id) {
                DB.rules.remove(i);
                return "rule deleted";
            }
        }
        return "rule not found";
    }
}
