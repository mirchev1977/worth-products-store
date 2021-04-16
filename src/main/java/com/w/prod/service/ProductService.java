package com.w.prod.service;

import com.w.prod.model.entity.UserEntity;
import com.w.prod.model.entity.enums.CategoryName;
import com.w.prod.model.service.ProductServiceModel;
import com.w.prod.model.view.ProductViewModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void add(ProductServiceModel map);

    BigDecimal getTotalSum();

    List<ProductViewModel> findAllProductsByCategoryName(CategoryName categoryName);

    void buyById(String id);

    void buyAll();

    List<ProductViewModel> getAllByUser(UserEntity user);

    int getTotalProductsSold(UserEntity user);

    void deleteById(String id);
}
