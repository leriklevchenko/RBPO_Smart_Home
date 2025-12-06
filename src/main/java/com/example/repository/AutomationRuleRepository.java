package com.example.repository;

import com.example.model.AutomationRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutomationRuleRepository extends JpaRepository<AutomationRule, Integer> {

    boolean existsByRoomIdAndActiveTrue(Integer roomId);

    List<AutomationRule> findByRoomIdAndEventTypeAndActiveTrue(Integer roomId, String eventType);
}
