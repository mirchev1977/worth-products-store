package com.worth.prod.service;

import com.worth.prod.model.entity.UserEntity;
import com.worth.prod.model.entity.enums.ArtistName;
import com.worth.prod.model.service.AlbumServiceModel;
import com.worth.prod.model.view.AlbumViewModel;

import java.math.BigDecimal;
import java.util.List;

public interface AlbumService {
    void add(AlbumServiceModel map);

    BigDecimal getTotalSum();

    List<AlbumViewModel> findAllAlbumsByArtistName(ArtistName artistName);

    void buyById(String id);

    void buyAll();

    List<AlbumViewModel> getAllByUser(UserEntity user);

    int getTotalAlbumsSold(UserEntity user);

    void deleteById(String id);
}
