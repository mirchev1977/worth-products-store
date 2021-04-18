package com.w.prod.repositories;

import com.w.prod.models.entity.Premise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PremiseRepository extends JpaRepository<Premise, String> {

    List<Premise> findAllByEquipment_EquipmentName(String equipmentName);

    Optional<Premise> findByName(String labName);
}
