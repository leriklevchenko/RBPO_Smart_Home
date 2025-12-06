package com.example.repository;

import com.example.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findTop1ByDeviceIdOrderByIdDesc(Integer deviceId);
}
