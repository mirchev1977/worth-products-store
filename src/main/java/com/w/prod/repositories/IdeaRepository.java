package com.w.prod.repositories;

import com.w.prod.models.entity.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, String> {
    List<Idea> findAllByOrderByStatusDesc();

    List<Idea> findAllByPromoterId(String id);

    Optional<Idea> findByName (String ideaName);
}
