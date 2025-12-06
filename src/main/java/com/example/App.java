package com.example;

import com.example.model.*;
import com.example.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * Инициализация тестовых данных:
     * - комнаты, устройства, пользователь домена, правила автоматизации
     * - пользователи для Spring Security (ADMIN и USER)
     */
    @Bean
    public CommandLineRunner initData(
            RoomRepository roomRepository,
            DeviceRepository deviceRepository,
            UserRepository userRepository,
            AutomationRuleRepository automationRuleRepository,
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (roomRepository.count() == 0) {
                Room living = new Room();
                living.setId(1);
                living.setName("Living room");
                roomRepository.save(living);

                Room kitchen = new Room();
                kitchen.setId(2);
                kitchen.setName("Kitchen");
                roomRepository.save(kitchen);

                Device livingLight = new Device();
                livingLight.setId(1);
                livingLight.setName("Living main light");
                livingLight.setRoom(living);
                livingLight.setType("light");
                livingLight.setOn(false);
                deviceRepository.save(livingLight);

                Device motionSensor = new Device();
                motionSensor.setId(2);
                motionSensor.setName("Living motion sensor");
                motionSensor.setRoom(living);
                motionSensor.setType("sensor");
                motionSensor.setOn(false);
                deviceRepository.save(motionSensor);

                Device kitchenLight = new Device();
                kitchenLight.setId(3);
                kitchenLight.setName("Kitchen light");
                kitchenLight.setRoom(kitchen);
                kitchenLight.setType("light");
                kitchenLight.setOn(false);
                deviceRepository.save(kitchenLight);

                User domainUser = new User();
                domainUser.setId(1);
                domainUser.setName("Alice");
                domainUser.setEmail("alice@example.com");
                userRepository.save(domainUser);

                AutomationRule rule1 = new AutomationRule();
                rule1.setId(1);
                rule1.setRoom(living);
                rule1.setEventType("motion");
                rule1.setAction("turn_on_light");
                rule1.setActive(true);
                automationRuleRepository.save(rule1);

                AutomationRule rule2 = new AutomationRule();
                rule2.setId(2);
                rule2.setRoom(living);
                rule2.setEventType("motion");
                rule2.setAction("notify_user");
                rule2.setActive(true);
                automationRuleRepository.save(rule2);
            }

            // Пользователи безопасности
            if (authUserRepository.count() == 0) {
                AuthUser admin = new AuthUser();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("Admin123!"));
                admin.setRole("ADMIN");
                admin.setEnabled(true);
                authUserRepository.save(admin);

                AuthUser user = new AuthUser();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("User123!"));
                user.setRole("USER");
                user.setEnabled(true);
                authUserRepository.save(user);
            }
        };
    }
}
