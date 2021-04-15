package com.worth.prod.service.impl;

import com.worth.prod.model.entity.CategoryEntity;
import com.worth.prod.model.entity.enums.CategoryName;
import com.worth.prod.repository.CategoryRepository;
import com.worth.prod.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private HashMap<String, String> careerInfo;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

        careerInfo = new HashMap<>();

        careerInfo.put("OFFICE_SUPPLIES", "Office Supplies Info");
        careerInfo.put("FURNITURE", "Furniture Supplies Info");
        careerInfo.put("BREAKROOM", "Break Room Items");
        careerInfo.put("SNACKS",    "Snack Supplies");
    }

    @Override
    public void initCategories() {
        if (categoryRepository.count() == 0) {
            Arrays.stream(CategoryName.values())
                    .forEach(categoryName -> {
                        CategoryEntity categoryEntity = new CategoryEntity(
                                categoryName,
                                this.careerInfo.get(categoryName.name())
                        );

                        categoryRepository.save(categoryEntity);
                    });
        }
    }

    @Override
    public CategoryEntity findByName(CategoryName categoryName) {
        return categoryRepository.findByName(categoryName).orElse(null);
    }
}
