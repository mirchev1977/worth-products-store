package com.w.prod.services.impl;

import com.w.prod.models.entity.Lab;
import com.w.prod.models.entity.Product;
import com.w.prod.repositories.LabRepository;
import com.w.prod.services.EquipmentService;
import com.w.prod.services.LabService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class LabServiceImpl implements LabService {
    //private final Resource labs;
    private final Gson gson;
    private final LabRepository labRepository;
    private final EquipmentService equipmentService;
    private final ModelMapper modelMapper;

    public LabServiceImpl(
            //@Value("classpath:init/labs.json") Resource labs,
            Gson gson,
            LabRepository labRepository,
            EquipmentService equipmentService,
            ModelMapper modelMapper
    ) {

        //this.labs = labs;
        this.gson = gson;
        this.labRepository = labRepository;
        this.equipmentService = equipmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedLabs() {
        if (labRepository.count() == 0) {
            String[][] labs = {
                { "Leonardo", "Wood workshop" },
                { "Tesla", "Metal workshop" },
                { "Lumiere", "Digital production workshop" },
                { "Bell", "Prototyping space" },
                { "Monnet", "Computers, Multimedia, Printers" },
                { "Blueprinttion", "Computers, Multimedia, Printers" },
                { "STEM&Art", "Computers, Multimedia, Printers" },
                { "Carnegie", "Computers, Multimedia, Printers" }
            };

            for (String[] l : labs ) {
                Lab lab = new Lab();
                lab.setName(l[0]);
                lab.setEquipment(equipmentService.findEquipment(l[1]));
                lab.setProducts(new ArrayList<>());
                labRepository.save(lab);
            }
        }
        //if (labRepository.count() == 0) {
        //    try {
        //        LabServiceModel[] labServiceModels = gson.fromJson(Files.readString(Path.of(labs.getURI())), LabServiceModel[].class);
        //        Arrays.stream(labServiceModels)
        //                .forEach(m -> {
        //                    List<Product> emptyList = new ArrayList<>();
        //                    Lab current = modelMapper.map(m, Lab.class);
        //                    current.setEquipment(equipmentService.findEquipment(m.getEquipment()));
        //                    current.setProducts(emptyList);
        //                    labRepository.save(current);
        //                });

        //    } catch (IOException e) {
        //        throw new IllegalStateException("Cannot seed Labs");
        //    }

        //}
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
    public Map<String, String> getSuitableLabsWithProducts(String neededEquipment) {

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
    public Map<String, String> getAllLabsWithProducts() {
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
        List<Product> products = l.getProducts();
        List<Product> copyOfProducts = new ArrayList<>(products);
        copyOfProducts.sort((p1, p2) -> p1.getStartDate().compareTo(p2.getStartDate()));
        StringBuilder sb = new StringBuilder();
        copyOfProducts.forEach(p -> {
            if (p.getEndDate().isAfter(LocalDate.now())) {
                String currentProduct =
                        String.format("%02d %s %s - %02d %s %s <br />",
                                p.getStartDate().getDayOfMonth(), p.getStartDate().getMonth(), p.getStartDate().getYear(),
                                p.getEndDate().getDayOfMonth(), p.getEndDate().getMonth(), p.getEndDate().getYear());
                sb.append(currentProduct);
            }
        });
        return sb.toString();
    }
}
