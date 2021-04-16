package com.w.prod.service;

import com.w.prod.model.entity.CategoryEntity;
import com.w.prod.model.entity.enums.CategoryName;

public interface CategoryService {
    void initCategories();

    CategoryEntity findByName(CategoryName categoryName);
}
