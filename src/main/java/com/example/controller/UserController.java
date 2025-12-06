package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/add")
    public Object add(@RequestParam int id, @RequestParam String name, @RequestParam String email) {
        if (userRepository.existsById(id)) {
            return "user with id " + id + " already exists";
        }
        User u = new User();
        u.setId(id);
        u.setName(name);
        u.setEmail(email);
        return userRepository.save(u);
    }

    @GetMapping
    public List<User> all() {
        return userRepository.findAll();
    }

    @PutMapping("/update")
    public Object update(@RequestParam int id,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String email) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "user not found";
        }
        if (name != null) {
            user.setName(name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        return userRepository.save(user);
    }

    @DeleteMapping("/delete")
    public Object delete(@RequestParam int id) {
        if (!userRepository.existsById(id)) {
            return "user not found";
        }
        userRepository.deleteById(id);
        return "user deleted";
    }
}
