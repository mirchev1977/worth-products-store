package com.worth.prod.repository;

import com.worth.prod.model.entity.AlbumEntity;
import com.worth.prod.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, String> {
//    @Query("SELECT SUM(p.price) FROM AlbumEntity p")
//    BigDecimal findTotalAlbumsSum();

//    List<AlbumEntity> findAllByArtist_Name(ArtistName artistName);
    List<AlbumEntity> findAllByAddedFromOrderByCopiesDesc(UserEntity userEntity);

    @Query("SELECT SUM(a.copies) from AlbumEntity a")
    int findAllByAddedFrom(UserEntity userEntity);
}
