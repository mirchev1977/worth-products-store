package com.w.prod.services.impl;

import com.w.prod.models.entity.Lab;
import com.w.prod.models.entity.Project;
import com.w.prod.models.service.LabServiceModel;
import com.w.prod.repositories.LabRepository;
import com.w.prod.services.EquipmentService;
import com.w.prod.services.LabService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class LabServiceImpl implements LabService {

    private final Resource labs;
    private final Gson gson;
    private final LabRepository labRepository;
    private final EquipmentService equipmentService;
    private final ModelMapper modelMapper;

    public LabServiceImpl(
            @Value("classpath:init/labs.json") Resource labs,
            Gson gson,
            LabRepository labRepository,
            EquipmentService equipmentService,
            ModelMapper modelMapper
    ) {

        this.labs = labs;
        this.gson = gson;
        this.labRepository = labRepository;
        this.equipmentService = equipmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedLabs() {
        if (labRepository.count() == 0) {
            try {
                LabServiceModel[] labServiceModels = gson.fromJson(Files.readString(Path.of(labs.getURI())), LabServiceModel[].class);
                Arrays.stream(labServiceModels)
                        .forEach(m -> {
                            List<Project> emptyList = new ArrayList<>();
                            Lab current = modelMapper.map(m, Lab.class);
                            current.setEquipment(equipmentService.findEquipment(m.getEquipment()));
                            current.setProjects(emptyList);
                            labRepository.save(current);
                        });

            } catch (IOException e) {
//                throw new IllegalStateException("Cannot seed Labs");
            }

        }
    }

    @Override
    public List<String> getAllLabs() {
        return labRepository.findAll().stream().map(Lab::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> findSuitableLabs(String providedEquipment) {
        return
                labRepository
                        .findAllByEquipment_EquipmentName(providedEquipment)
                        .stream()
                        .map(l -> l.getName())
                        .collect(Collectors.toList());
    }

    @Override
    public Lab findLab(String labName) {
        return labRepository.findByName(labName).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Map<String, String> getSuitableLabsWithProjects(String neededEquipment) {

        Map<String, String> info = new TreeMap<>();
        List<Lab> labs = labRepository
                .findAllByEquipment_EquipmentName(neededEquipment);
        labs
                .forEach(l -> {
                    String infoForLab = getInfoForLab(l);
                    info.put(l.getName(), infoForLab);
                });

        return info;
    }

    @Override
    public Map<String, String> getAllLabsWithProjects() {
        Map<String, String> info = new TreeMap<>();
        List<Lab> labs = labRepository
                .findAll();
        labs
                .forEach(l -> {
                    String infoForLab = getInfoForLab(l);
                    info.put(l.getName(), infoForLab);
                });

        return info;
    }

    private String getInfoForLab(Lab l) {
        List<Project> projects = l.getProjects();
        List<Project> copyOfProjects = new ArrayList<>(projects);
        copyOfProjects.sort((p1, p2) -> p1.getStartDate().compareTo(p2.getStartDate()));
        StringBuilder sb = new StringBuilder();
        copyOfProjects.forEach(p -> {
            if (p.getEndDate().isAfter(LocalDate.now())) {
                String currentProject =
                        String.format("%02d %s %s - %02d %s %s <br />",
                                p.getStartDate().getDayOfMonth(), p.getStartDate().getMonth(), p.getStartDate().getYear(),
                                p.getEndDate().getDayOfMonth(), p.getEndDate().getMonth(), p.getEndDate().getYear());
                sb.append(currentProject);
            }
        });
        return sb.toString();
    }
}
