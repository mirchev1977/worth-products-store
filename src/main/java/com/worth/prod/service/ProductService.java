package com.worth.prod.service;

import com.worth.prod.model.entity.UserEntity;
import com.worth.prod.model.entity.enums.ArtistName;
import com.worth.prod.model.service.ProductServiceModel;
import com.worth.prod.model.view.ProductViewModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void add(ProductServiceModel map);

    BigDecimal getTotalSum();

    List<ProductViewModel> findAllProductsByArtistName(ArtistName artistName);

    void buyById(String id);

    void buyAll();

    List<ProductViewModel> getAllByUser(UserEntity user);

    int getTotalProductsSold(UserEntity user);

    void deleteById(String id);
}
