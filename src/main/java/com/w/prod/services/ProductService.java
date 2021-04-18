package com.w.prod.services;

import com.w.prod.models.service.ProductResultServiceModel;
import com.w.prod.models.service.ProductServiceModel;
import com.w.prod.models.view.ProductBasicViewModel;
import com.w.prod.models.view.ProductDetailedViewModel;
import com.w.prod.models.view.ProductResultViewModel;

import java.util.List;

public interface ProductService {
    ProductBasicViewModel createProduct(ProductServiceModel productServiceModel);

    List<ProductBasicViewModel> getActiveProductsOrderedbyStartDate();

    ProductDetailedViewModel extractProductModel(String id);

    List<String> deleteProduct(String id);

    List<ProductBasicViewModel> getUserProductsOrderedByStartDate(String username);

    void archiveProduct(String id);

    String findProductOwnerStr(String id);

    boolean joinProduct(String id, String userName);

    boolean checkIfCollaborating(String id, String userName);

    void leaveProduct(String id, String userName);

    ProductServiceModel extractProductServiceModel(String id);

    String getProductPromoter(String id);

    void updateProduct(String id, ProductServiceModel productServiceModel);

    void publishProductResult(ProductResultServiceModel productServiceModel);

    ProductResultServiceModel extractProductResultServiceModel(String id);

    List<ProductResultViewModel> getResults(String it);

    List<ProductBasicViewModel> getUserCollaborationsOrderedByStartDate(String userName);

    long getDurationInDays(ProductServiceModel productAddBindingModel);

    void deleteProductsOfUser(String id);

    ProductServiceModel findProductById(String productId);
}
