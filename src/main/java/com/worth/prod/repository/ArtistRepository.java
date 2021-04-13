package com.worth.prod.repository;

import com.worth.prod.model.entity.ArtistEntity;
import com.worth.prod.model.entity.enums.ArtistName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, String> {
    Optional<ArtistEntity> findByName(ArtistName name);
}
