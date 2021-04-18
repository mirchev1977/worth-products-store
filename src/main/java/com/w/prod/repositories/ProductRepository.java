package com.w.prod.repositories;

import com.w.prod.models.entity.Product;
import com.w.prod.models.entity.UserEntity;
import com.w.prod.models.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByActiveTrueOrderByStartDateAsc();
    List<Product> findAllByActiveAndPromoterOrderByStartDate(boolean active, UserEntity promoter);

    @Query("SELECT p FROM Product p WHERE p.result is not null and p.category= :category ")
    List<Product> findAllResultsByCategory(@Param("category") Category category);
    List<Product> findAllByPromoterId(String id);

}
