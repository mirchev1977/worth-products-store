package com.w.prod.repositories;

import com.w.prod.models.entity.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface LabRepository extends JpaRepository<Lab, String> {

    List<Lab> findAllByEquipment_EquipmentName(String equipmentName);

    Optional<Lab> findByName(String labName);
}
