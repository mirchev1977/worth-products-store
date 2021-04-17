package com.w.prod.repositories;

import com.w.prod.models.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, String> {
    Optional<ActivityType> findByActivityName(String activityName);
List<ActivityType> findAll ();
}
