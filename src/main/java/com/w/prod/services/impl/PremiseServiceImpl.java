package com.w.prod.services.impl;

import com.w.prod.models.entity.Premise;
import com.w.prod.models.entity.Product;
import com.w.prod.repositories.PremiseRepository;
import com.w.prod.services.EquipmentService;
import com.w.prod.services.PremiseService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PremiseServiceImpl implements PremiseService {
    //private final Resource premises;
    private final Gson gson;
    private final PremiseRepository premiseRepository;
    private final EquipmentService equipmentService;
    private final ModelMapper modelMapper;

    public PremiseServiceImpl(
            //@Value("classpath:init/premises.json") Resource premises,
            Gson gson,
            PremiseRepository premiseRepository,
            EquipmentService equipmentService,
            ModelMapper modelMapper
    ) {

        //this.premises = premises;
        this.gson = gson;
        this.premiseRepository = premiseRepository;
        this.equipmentService = equipmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedPremises() {
        if (premiseRepository.count() == 0) {
            String[][] premises = {
                { "Sofia_Lab", "Wood workshop" },
                { "Varna_Factory", "Metal workshop" },
                { "Partner_Storage", "Digital production workshop" },
                { "Abroad", "Prototyping space" },
                { "Local_Laboratory", "Computers, Multimedia, Printers" },
                { "Quality_Review", "Computers, Multimedia, Printers" },
                { "Material_Design", "Computers, Multimedia, Printers" },
                { "Waiting_Room", "Computers, Multimedia, Printers" }
            };

            for (String[] l : premises ) {
                Premise premise = new Premise();
                premise.setName(l[0]);
                premise.setEquipment(equipmentService.findEquipment(l[1]));
                premise.setProducts(new ArrayList<>());
                premiseRepository.save(premise);
            }
        }
        //if (labRepository.count() == 0) {
        //    try {
        //        PremiseServiceModel[] labServiceModels = gson.fromJson(Files.readString(Path.of(labs.getURI())), PremiseServiceModel[].class);
        //        Arrays.stream(labServiceModels)
        //                .forEach(m -> {
        //                    List<Product> emptyList = new ArrayList<>();
        //                    Premise current = modelMapper.map(m, Premise.class);
        //                    current.setEquipment(equipmentService.findEquipment(m.getEquipment()));
        //                    current.setProducts(emptyList);
        //                    labRepository.save(current);
        //                });

        //    } catch (IOException e) {
        //        throw new IllegalStateException("Cannot seed Premises");
        //    }

        //}
    }

    @Override
    public List<String> getAllPremises() {
        return premiseRepository.findAll().stream().map(Premise::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> findSuitablePremises(String providedEquipment) {
        return
                premiseRepository
                        .findAllByEquipment_EquipmentName(providedEquipment)
                        .stream()
                        .map(l -> l.getName())
                        .collect(Collectors.toList());
    }

    @Override
    public Premise findPremise(String labName) {
        return premiseRepository.findByName(labName).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Map<String, String> getSuitablePremisesWithProducts(String neededEquipment) {

        Map<String, String> info = new TreeMap<>();
        List<Premise> premises = premiseRepository
                .findAllByEquipment_EquipmentName(neededEquipment);
        premises
                .forEach(l -> {
                    String infoForPremise = getInfoForPremise(l);
                    info.put(l.getName(), infoForPremise);
                });

        return info;
    }

    @Override
    public Map<String, String> getAllPremisesWithProducts() {
        Map<String, String> info = new TreeMap<>();
        List<Premise> premises = premiseRepository
                .findAll();
        premises
                .forEach(l -> {
                    String infoForPremise = getInfoForPremise(l);
                    info.put(l.getName(), infoForPremise);
                });

        return info;
    }

    private String getInfoForPremise(Premise l) {
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
