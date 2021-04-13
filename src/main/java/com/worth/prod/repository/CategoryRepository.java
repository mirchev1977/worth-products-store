package com.worth.prod.repository;

import com.worth.prod.model.entity.CategoryEntity;
import com.worth.prod.model.entity.enums.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    Optional<CategoryEntity> findByName(CategoryName name);
}
