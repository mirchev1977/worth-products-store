package com.worth.prod.service.impl;

import com.worth.prod.model.entity.ProductEntity;
import com.worth.prod.model.entity.UserEntity;
import com.worth.prod.model.entity.enums.ArtistName;
import com.worth.prod.model.service.ProductServiceModel;
import com.worth.prod.model.view.ProductViewModel;
import com.worth.prod.repository.ProductRepository;
import com.worth.prod.service.ProductService;
import com.worth.prod.service.ArtistService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ArtistService artistService;
    private final ModelMapper modelMapper;
    private final HttpSession httpSession;

    public ProductServiceImpl(ProductRepository productRepository, ArtistService artistService, ModelMapper modelMapper, HttpSession httpSession) {
        this.productRepository = productRepository;
        this.artistService = artistService;
        this.modelMapper = modelMapper;
        this.httpSession = httpSession;
    }

    @Override
    public void add(ProductServiceModel productServiceModel) {
        ProductEntity productEntity = modelMapper.map(productServiceModel, ProductEntity.class);
        productEntity.setArtistEntity(artistService.findByName(productServiceModel.getArtist()));
        productEntity.setAddedFrom(modelMapper.map(httpSession.getAttribute("user"),
                UserEntity.class
                ));

        productRepository.save(productEntity);
    }

    @Override
    public BigDecimal getTotalSum() {
//        return productRepository.findTotalProductsSum();
        return null;
    }

    @Override
    public List<ProductViewModel> findAllProductsByArtistName(ArtistName artistName) {
//        return productRepository.findAllByArtist_Name(artistName)
//                .stream().map(productEntity -> modelMapper.map(productEntity, ProductViewModel.class))
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public void buyById(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public void buyAll() {
        productRepository.deleteAll();
    }

    @Override
    public List<ProductViewModel> getAllByUser(UserEntity user) {
        return productRepository.findAllByAddedFromOrderByCopiesDesc(user).stream().map(productEntity ->
                modelMapper.map(productEntity, ProductViewModel.class)).collect(Collectors.toList());
    }

    @Override
    public int getTotalProductsSold(UserEntity user) {
        return productRepository.findAllByAddedFrom(user);
    }

    @Override
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }
}
