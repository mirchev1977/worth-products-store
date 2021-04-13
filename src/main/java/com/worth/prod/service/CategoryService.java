package com.worth.prod.service;

import com.worth.prod.model.entity.CategoryEntity;
import com.worth.prod.model.entity.enums.CategoryName;

public interface CategoryService {
    void initCategories();

    CategoryEntity findByName(CategoryName categoryName);
}
