package com.worth.prod.service;

import com.worth.prod.model.entity.ArtistEntity;
import com.worth.prod.model.entity.enums.ArtistName;

public interface ArtistService {
    void initArtists();

    ArtistEntity findByName(ArtistName artistName);
}
