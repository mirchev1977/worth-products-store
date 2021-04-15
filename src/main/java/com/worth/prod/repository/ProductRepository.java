package com.worth.prod.repository;

import com.worth.prod.model.entity.ProductEntity;
import com.worth.prod.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {
//    @Query("SELECT SUM(p.price) FROM ProductEntity p")
//    BigDecimal findTotalProductsSum();

//    List<ProductEntity> findAllByArtist_Name(CategoryName categoryName);
    List<ProductEntity> findAllByAddedFromOrderByQuantityDesc(UserEntity userEntity);

    @Query("SELECT SUM(a.quantity) from ProductEntity a")
    int findAllByAddedFrom(UserEntity userEntity);
}
