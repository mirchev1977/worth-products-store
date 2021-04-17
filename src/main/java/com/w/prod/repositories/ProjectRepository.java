package com.w.prod.repositories;

import com.w.prod.models.entity.Project;
import com.w.prod.models.entity.UserEntity;
import com.w.prod.models.entity.enums.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findAllByActiveTrueOrderByStartDateAsc();
    List<Project> findAllByActiveAndPromoterOrderByStartDate(boolean active, UserEntity promoter);

    @Query("SELECT p FROM Project p WHERE p.result is not null and p.sector= :sector ")
    List<Project> findAllResultsBySector(@Param("sector") Sector sector );
    List<Project> findAllByPromoterId(String id);

}
