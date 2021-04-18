package com.w.prod.repositories;

import com.w.prod.models.entity.Blueprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlueprintRepository extends JpaRepository<Blueprint, String> {
    List<Blueprint> findAllByOrderByStatusDesc();

    List<Blueprint> findAllByPromoterId(String id);

    Optional<Blueprint> findByName (String blueprintName);
}
