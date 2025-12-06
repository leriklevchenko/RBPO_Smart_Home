package com.example.repository;

import com.example.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

    boolean existsByRoomId(Integer roomId);

    List<Device> findByRoomId(Integer roomId);

    List<Device> findByRoomIdAndType(Integer roomId, String type);
}
