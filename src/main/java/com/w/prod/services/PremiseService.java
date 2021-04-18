package com.w.prod.services;

import com.w.prod.models.entity.Premise;

import java.util.List;
import java.util.Map;

public interface PremiseService {

    void seedPremises();

    List<String> getAllPremises();

    List<String> findSuitablePremises(String providedEquipment);

    Premise findPremise(String premiseName);

    Map<String, String> getSuitablePremisesWithProducts(String neededEquipment);

    Map<String, String> getAllPremisesWithProducts();
}

