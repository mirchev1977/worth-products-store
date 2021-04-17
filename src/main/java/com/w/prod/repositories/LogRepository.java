package com.w.prod.repositories;

import com.w.prod.models.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, String> {

    List<LogEntity> findAllByProjectNotNullOrderByTimeDesc();

    List<LogEntity> findAllByIdeaNotNullOrderByTimeDesc();

    List<LogEntity> findByIdea_Id(String id);

    List<LogEntity> findByProject_Id(String id);

    List<LogEntity> findByUser_Id(String id);
}
