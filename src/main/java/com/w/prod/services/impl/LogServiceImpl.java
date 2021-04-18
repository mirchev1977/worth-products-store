package com.w.prod.services.impl;

import com.w.prod.models.entity.Blueprint;
import com.w.prod.models.entity.LogEntity;
import com.w.prod.models.entity.Product;
import com.w.prod.models.entity.UserEntity;
import com.w.prod.models.service.BlueprintLogServiceModel;
import com.w.prod.models.service.ProductServiceModel;
import com.w.prod.models.view.AddBlueprintLogViewModel;
import com.w.prod.models.view.JoinProductLogViewModel;
import com.w.prod.repositories.LogRepository;
import com.w.prod.services.BlueprintService;
import com.w.prod.services.LogService;
import com.w.prod.services.ProductService;
import com.w.prod.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final UserService userService;
    private final ProductService productService;
    private final BlueprintService blueprintService;
    private final ModelMapper modelMapper;
    private Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);


    public LogServiceImpl(LogRepository logRepository, UserService userService, ProductService productService, BlueprintService blueprintService, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.userService = userService;
        this.productService = productService;
        this.blueprintService = blueprintService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createProductJoinLog(String action, String productId) {

        ProductServiceModel product = productService.findProductById(productId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userEntity = userService.findByUsername(username);

        LogEntity logEntity = new LogEntity()
                .setProduct(modelMapper.map(product, Product.class))
                .setAction(action)
                .setTime(LocalDateTime.now())
                .setUser(userEntity);
        logRepository.save(logEntity);
    }


    @Override
    public void createBlueprintAddLog(String action, String blueprintName) {
        BlueprintLogServiceModel blueprint = blueprintService.generateBlueprintServiceModel(blueprintName);

if (blueprint!=null) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    UserEntity userEntity = userService.findByUsername(username);
    LogEntity logEntity = new LogEntity()
            .setBlueprint(modelMapper.map(blueprint, Blueprint.class))
            .setAction(action)
            .setTime(LocalDateTime.now())
            .setUser(userEntity);
    logRepository.save(logEntity);
}

    }

    @Override
    public List<AddBlueprintLogViewModel> findAllBlueprintAddLogs() {
        return logRepository
                .findAllByBlueprintNotNullOrderByTimeDesc()
                .stream()
                .map(l -> {
                    AddBlueprintLogViewModel addBlueprintLogViewModel = modelMapper.map(l, AddBlueprintLogViewModel.class);
                    addBlueprintLogViewModel
                            .setBlueprint(l.getBlueprint().getName())
                            .setUser(l.getUser().getUsername())
                            .setDateTime(String.format("%02d %s %s (%02d:%02d)",
                                    l.getTime().getDayOfMonth(), l.getTime().getMonth(),
                                    l.getTime().getYear(), l.getTime().getHour(), l.getTime().getMinute()));
                    return addBlueprintLogViewModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<JoinProductLogViewModel> findAllJoinProductLogs() {
        return logRepository
                .findAllByProductNotNullOrderByTimeDesc()
                .stream()
                .map(l -> {
                    JoinProductLogViewModel joinProductLogViewModel = modelMapper.map(l, JoinProductLogViewModel.class);
                    joinProductLogViewModel
                            .setProduct(l.getProduct().getName())
                            .setUser(l.getUser().getUsername())
                            .setDateTime(String.format("%02d %s %s (%02d:%02d)",
                                    l.getTime().getDayOfMonth(), l.getTime().getMonth(),
                                    l.getTime().getYear(), l.getTime().getHour(), l.getTime().getMinute()));
                    return joinProductLogViewModel;
                })
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0/10 20 * * FRI")
    public void deleteLogs() {
        LOGGER.info("Deleting logs...");
        logRepository.deleteAll();
    }

    public Map<Integer, Integer> getStatsJoinProductActivity() {
        Map<Integer, Integer> activityMap = new HashMap<>();
        logRepository.findAllByProductNotNullOrderByTimeDesc()
                .forEach(l -> {
                    int dayOfWeek = l.getTime().getDayOfWeek().getValue();
                    activityMap.putIfAbsent(dayOfWeek, 0);
                    activityMap.put(dayOfWeek, activityMap.get(dayOfWeek) + 1);
                });
        return activityMap;
    }


    public Map<Integer, Integer> getStatsBlueprintsCreated() {
        Map<Integer, Integer> activityMap = new HashMap<>();
        logRepository.findAllByBlueprintNotNullOrderByTimeDesc()
                .forEach(l -> {
                    int dayOfWeek = l.getTime().getDayOfWeek().getValue();
                    activityMap.putIfAbsent(dayOfWeek, 0);
                    activityMap.put(dayOfWeek, activityMap.get(dayOfWeek) + 1);
                });
        return activityMap;
    }
}
