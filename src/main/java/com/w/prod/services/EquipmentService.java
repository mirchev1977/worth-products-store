package com.w.prod.services;

import com.w.prod.models.entity.Equipment;

import java.util.List;

public interface EquipmentService {
    void seedEquipment();

    Equipment findEquipment(String equipmentName);

    List<String> getAllEquipments();
}
