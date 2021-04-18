package com.w.prod.services.impl;

import com.w.prod.models.entity.*;
import com.w.prod.models.entity.enums.Category;
import com.w.prod.models.service.ProductResultServiceModel;
import com.w.prod.models.service.ProductServiceModel;
import com.w.prod.models.view.ProductBasicViewModel;
import com.w.prod.models.view.ProductDetailedViewModel;
import com.w.prod.models.view.ProductResultViewModel;
import com.w.prod.repositories.ActivityTypeRepository;
import com.w.prod.repositories.EquipmentRepository;
import com.w.prod.repositories.LogRepository;
import com.w.prod.repositories.ProductRepository;
import com.w.prod.services.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final LogRepository logRepository;
    private final UserService userService;
    private final ActivityTypeRepository activityTypeRepository;
    private final PremiseService premiseService;
    private final EquipmentRepository equipmentRepository;
    private Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository, LogRepository logRepository, ModelMapper modelMapper, UserService userService, ActivityTypeRepository activityTypeRepository, PremiseService premiseService, EquipmentRepository equipmentRepository) {
        this.productRepository = productRepository;
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.activityTypeRepository = activityTypeRepository;
        this.premiseService = premiseService;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public ProductBasicViewModel createProduct(ProductServiceModel productServiceModel) {

        Product product = modelMapper.map(productServiceModel, Product.class);
        product.setPromoter(userService.findByUsername(productServiceModel.getPromoter()))
                .setActivityType(activityTypeRepository.findByActivityName(productServiceModel.getActivityType()).orElseThrow(NullPointerException::new))
                .setPremise(premiseService.findPremise(productServiceModel.getPremise()))
                .setNeededEquipment(equipmentRepository.findByEquipmentName(productServiceModel.getNeededEquipment()).orElseThrow(NullPointerException::new));

        productRepository.save(product);
        ProductBasicViewModel viewModel = modelMapper.map(product, ProductBasicViewModel.class);
        viewModel
                .setPremise(product.getPremise().getName())
                .setStartDate(product.getStartDate().toString());
        return viewModel;
    }

    @Override
    public List<ProductBasicViewModel> getActiveProductsOrderedbyStartDate() {
        return productRepository
                .findAllByActiveTrueOrderByStartDateAsc()
                .stream()
                .map(p -> mapProduct(p))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDetailedViewModel extractProductModel(String id) {
        Product product = productRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        ProductDetailedViewModel productViewModel = modelMapper.map(product, ProductDetailedViewModel.class);
        String firstName = userService.findByUsername(product.getPromoter().getUsername()).getFirstName();
        String lastName = userService.findByUsername(product.getPromoter().getUsername()).getLastName();
        productViewModel.setPromoter(String.format("%s %s", firstName, lastName))
                .setActivityType(product.getActivityType().getActivityName())
                .setPremise(product.getPremise().getName())
                .setNeededEquipment(product.getNeededEquipment().getEquipmentName());

        String duration = String.format("%02d %s %s - %02d %s %s <br />",
                product.getStartDate().getDayOfMonth(), product.getStartDate().getMonth(), product.getStartDate().getYear(),
                product.getEndDate().getDayOfMonth(), product.getEndDate().getMonth(), product.getEndDate().getYear());
        productViewModel.setDuration(duration);

        StringBuilder sb = new StringBuilder();
        product.getCollaborators().forEach(c -> {
            sb.append(String.format("%s %s<br />", c.getFirstName(), c.getLastName()));
        });
        String collaborators = sb.toString();
        productViewModel.setCollaborators(collaborators);
        return productViewModel;

    }

    @Override
    public List<String> deleteProduct(String id) {
        List<LogEntity> logs = logRepository.findByProduct_Id(id);
        List<String> deletedLogs = new ArrayList<>();
        if (!logs.isEmpty()) {
            logs.forEach(l -> {
                deletedLogs.add(l.getProduct().getName());
                logRepository.delete(l);
            });
        }
        LOGGER.info("Deleted logs for products: {}", String.join(", ", deletedLogs));
        productRepository.deleteById(id);
        return deletedLogs;
    }

    @Override
    public List<ProductBasicViewModel> getUserProductsOrderedByStartDate(String username) {
        UserEntity user = userService.findByUsername(username);
        return productRepository
                .findAllByActiveAndPromoterOrderByStartDate(true, user)
                .stream()
                .map(p -> mapProduct(p))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductBasicViewModel> getUserCollaborationsOrderedByStartDate(String userName) {
        return userService
                .getProductsByUser(userName)
                .stream()
                .sorted((a, b) -> a.getStartDate().compareTo(b.getStartDate()))
                .map(p -> mapProduct(p))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProductsOfUser(String id) {
        List<String> deletedProducts = new ArrayList<>();
        productRepository.findAllByPromoterId(id)
                .forEach(p -> {
                    deletedProducts.add(p.getName());
                    deleteProduct(p.getId());
                });
    }

    @Override
    public ProductServiceModel findProductById(String productId) {
        Product product = productRepository.getOne(productId);
        ProductServiceModel model = modelMapper.map(product, ProductServiceModel.class);
        model.setPromoter(product.getPromoter().getUsername());
        model.setPremise(product.getPremise().getName());
        return model;
    }

    @Override
    public void archiveProduct(String id) {
        Product product = productRepository.getOne(id);
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public String findProductOwnerStr(String id) {
        return this.productRepository.getOne(id).getPromoter().getUsername();
    }

    @Override
    public boolean joinProduct(String id, String userName) {
        try {
            UserEntity user = userService.findByUsername(userName);
            Product product = productRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            System.out.println();
            user.addProduct(product);
            product.addCollaborator(user);
            userService.updateUser(user);
            productRepository.save(product);
            return true;
        } catch (Exception ex) {
            throw new IllegalArgumentException("User could not join product");
        }

    }

    @Override
    public boolean checkIfCollaborating(String id, String userName) {
        UserEntity user = userService.findByUsername(userName);
        Product product = productRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return user.getProducts().contains(product);
    }

    @Override
    public void leaveProduct(String id, String userName) {
        try {
            UserEntity user = userService.findByUsername(userName);
            Product product = productRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            user.removeProduct(product);
            product.removeCollaborator(user);
            userService.updateUser(user);
            productRepository.save(product);
        } catch (Exception ex) {
            throw new IllegalArgumentException("User could not leave product");
        }
    }

    @Override
    public ProductServiceModel extractProductServiceModel(String id) {
        Product product = productRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        ProductServiceModel productServiceModel = modelMapper.map(product, ProductServiceModel.class)
                .setPromoter(product.getPromoter().getUsername())
                .setActivityType(product.getActivityType().getActivityName())
                .setPremise(product.getPremise().getName())
                .setNeededEquipment(product.getNeededEquipment().getEquipmentName());

        return productServiceModel;
    }

    @Override
    public String getProductPromoter(String id) {
        return productRepository.getOne(id).getPromoter().getUsername();
    }

    @Override
    public void updateProduct(String id, ProductServiceModel productServiceModel) {
        Product product = productRepository.getOne(id)
                .setName(productServiceModel.getName())
                .setDescription(productServiceModel.getDescription());
        ActivityType currentActivity = activityTypeRepository.findByActivityName(productServiceModel.getActivityType()).orElseThrow(NullPointerException::new);
        product.setActivityType(currentActivity)
                .setCategory(productServiceModel.getCategory())
                .setStartDate(productServiceModel.getStartDate())
                .setEndDate(productServiceModel.getEndDate());
        Equipment currentEquipment = equipmentRepository.findByEquipmentName(productServiceModel.getNeededEquipment()).orElseThrow(NullPointerException::new);
        product.setNeededEquipment(currentEquipment);
        Premise currentPremise = premiseService.findPremise(productServiceModel.getPremise());
        product.setPremise(currentPremise);
        productRepository.save(product);
    }

    @Override
    public void publishProductResult(ProductResultServiceModel productServiceModel) {
        Product product = productRepository.getOne(productServiceModel.getId());
        product.setResult(productServiceModel.getDescription());
        productRepository.save(product);
    }

    @Override
    public ProductResultServiceModel extractProductResultServiceModel(String id) {
        Product product = productRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return modelMapper.map(product, ProductResultServiceModel.class);
    }

    @Override
    public List<ProductResultViewModel> getResults(String param) {
        Category category = Category.valueOf(param);
        return productRepository.findAllResultsByCategory(category)
                .stream()
                .map(p -> modelMapper.map(p, ProductResultViewModel.class))
                .collect(Collectors.toList());
    }


    private ProductBasicViewModel mapProduct(Product p) {
        ProductBasicViewModel productViewModel = modelMapper.map(p, ProductBasicViewModel.class);
        productViewModel.setActivityType(p.getActivityType().getActivityName())
                .setPremise(p.getPremise().getName());

        String startDate = String.format("%02d %s %s",
                p.getStartDate().getDayOfMonth(), p.getStartDate().getMonth(),
                p.getStartDate().getYear());

        productViewModel.setStartDate(startDate);

        return productViewModel;
    }

    public long getDurationInDays(ProductServiceModel productServiceModel) {
        Instant startDate = productServiceModel.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endDate = productServiceModel.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant();

        return Duration.between(startDate, endDate).toDays();
    }

}

