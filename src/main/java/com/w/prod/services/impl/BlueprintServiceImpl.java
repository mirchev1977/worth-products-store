package com.w.prod.services.impl;

import com.w.prod.models.entity.Blueprint;
import com.w.prod.models.entity.LogEntity;
import com.w.prod.models.entity.UserEntity;
import com.w.prod.models.service.BlueprintLogServiceModel;
import com.w.prod.models.service.BlueprintServiceModel;
import com.w.prod.models.view.BlueprintViewModel;
import com.w.prod.repositories.*;
import com.w.prod.services.BlueprintService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlueprintServiceImpl implements BlueprintService {
    private Logger LOGGER = LoggerFactory.getLogger(BlueprintServiceImpl.class);

    private final BlueprintRepository blueprintRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final LogRepository logRepository;
    private final EquipmentRepository equipmentRepository;

    public BlueprintServiceImpl(BlueprintRepository blueprintRepository, ModelMapper modelMapper, UserRepository userRepository, ActivityTypeRepository activityTypeRepository, LogRepository logRepository, EquipmentRepository equipmentRepository) {
        this.blueprintRepository = blueprintRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.equipmentRepository = equipmentRepository;
        this.activityTypeRepository = activityTypeRepository;
    }

    @Override
    public BlueprintViewModel createBlueprint(BlueprintServiceModel blueprintServiceModel) {
        Blueprint blueprint = modelMapper.map(blueprintServiceModel, Blueprint.class);
        blueprint.setActivityType(activityTypeRepository.findByActivityName(blueprintServiceModel.getActivityType()).orElseThrow(NullPointerException::new));
        blueprint.setNeededEquipment(equipmentRepository.findByEquipmentName(blueprintServiceModel.getNeededEquipment()).orElseThrow(NullPointerException::new));
        blueprint.setPromoter(userRepository.findByUsername(blueprintServiceModel.getPromoter()).orElseThrow(NullPointerException::new));

        blueprintRepository.save(blueprint);

        BlueprintViewModel blueprintViewModel = modelMapper.map(blueprint, BlueprintViewModel.class);
        blueprintViewModel.setNeededEquipment(blueprint.getNeededEquipment().getEquipmentName());
        blueprintViewModel.setPromoter(blueprint.getPromoter().getUsername());
        blueprintViewModel.setActivityType(blueprint.getActivityType().getActivityName());
        return blueprintViewModel;
    }

    @Override
    @Transactional
    public List<BlueprintViewModel> getAll() {
        return blueprintRepository
                .findAllByOrderByStatusDesc()
                .stream()
                .map(i -> mapBlueprint(i))
                .collect(Collectors.toList());
    }

    @Override
    public BlueprintServiceModel extractBlueprintModel(String id) {
        Blueprint blueprint = blueprintRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        BlueprintServiceModel blueprintServiceModel = modelMapper.map(blueprint, BlueprintServiceModel.class);
        blueprintServiceModel
                .setPromoter(blueprint.getPromoter().getUsername())
                .setActivityType(blueprint.getActivityType().getActivityName())
                .setNeededEquipment(blueprint.getNeededEquipment().getEquipmentName());

        return blueprintServiceModel;
    }

    @Override
    public List<String> deleteBlueprint(String id) {
        List<LogEntity> logs = logRepository.findByBlueprint_Id(id);
        List<String> deletedLoggedBlueprintNames = new ArrayList<>();
        if (!logs.isEmpty()) {
            logs.forEach(l -> {
                deletedLoggedBlueprintNames.add(l.getBlueprint().getName());
                logRepository.delete(l);
            });
        }
        deletedLoggedBlueprintNames.forEach(l -> LOGGER.info("Deleted logs for blueprint : {}", l));
        blueprintRepository.deleteById(id);
        return deletedLoggedBlueprintNames;
    }

    @Override
    public boolean markBlueprintAsAccepted(String id) {
        Optional<Blueprint> currentBlueprint = blueprintRepository.findById(id);
        if (currentBlueprint.isPresent()) {
            Blueprint blueprint = currentBlueprint.get();
            blueprint.setStatus("Accepted");
            blueprintRepository.save(blueprint);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getDurationOfBlueprint(String id) {
        int duration = 0;
        if (blueprintRepository.findById(id).isPresent()) {
            duration = blueprintRepository.findById(id).get().getDuration();
        }
        return duration;
    }

    @Override
    public BlueprintViewModel getBlueprintView(String id) {
        Blueprint blueprint = blueprintRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return mapBlueprint(blueprint);
    }

    @Override
    public List<String> deleteBlueprintsOfUser(String id) {
        List<Blueprint> blueprints = blueprintRepository.findAllByPromoterId(id);
        List<String> deletedBlueprintsNames = new ArrayList<>();
        blueprints.forEach(i -> {
            deletedBlueprintsNames.add(i.getName());
            deleteBlueprint(i.getId());
        });
        deletedBlueprintsNames.forEach(l -> LOGGER.info("Deleted the following blueprints {}", String.join(",", deletedBlueprintsNames)));
        return deletedBlueprintsNames;
    }

    @Override
    public BlueprintLogServiceModel generateBlueprintServiceModel(String blueprintName) {
        Blueprint blueprint = blueprintRepository.findByName(blueprintName).orElse(null);
        if (blueprint != null) {
            BlueprintLogServiceModel blueprintLogServiceModel = modelMapper.map(blueprint, BlueprintLogServiceModel.class);
            blueprintLogServiceModel.setPromoter(blueprint.getPromoter().getUsername())
                    .setActivityType(blueprint.getActivityType().getActivityName())
                    .setNeededEquipment(blueprint.getNeededEquipment().getEquipmentName());
            return blueprintLogServiceModel;
        }
        return null;
    }


    BlueprintViewModel mapBlueprint(Blueprint blueprint) {
        BlueprintViewModel blueprintViewModel = modelMapper.map(blueprint, BlueprintViewModel.class);
        UserEntity user = userRepository.findByUsername(blueprint.getPromoter().getUsername()).orElseThrow(NullPointerException::new);
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        blueprintViewModel.setPromoter(String.format("%s %s", firstName, lastName))
                .setActivityType(blueprint.getActivityType().getActivityName())
                .setNeededEquipment(blueprint.getNeededEquipment().getEquipmentName());
        return blueprintViewModel;
    }
}
