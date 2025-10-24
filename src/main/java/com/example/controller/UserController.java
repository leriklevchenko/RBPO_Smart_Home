package com.example.controller;

import com.example.model.User;
import com.example.storage.DB;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping("/add")
    public Object add(@RequestParam int id, @RequestParam String name, @RequestParam String email) {
        User u = new User(id, name, email);
        DB.users.add(u);
        return u;
    }

    @GetMapping
    public List<User> all() {
        return DB.users;
    }

    @PutMapping("/update")
    public Object update(@RequestParam int id,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String email) {
        for (User u : DB.users) {
            if (u.getId() == id) {
                if (name != null) u.setName(name);
                if (email != null) u.setEmail(email);
                return u;
            }
        }
        return "user not found";
    }

    @DeleteMapping("/delete")
    public Object delete(@RequestParam int id) {
        for (int i = 0; i < DB.users.size(); i++) {
            if (DB.users.get(i).getId() == id) {
                DB.users.remove(i);
                return "user deleted";
            }
        }
        return "user not found";
    }
}
