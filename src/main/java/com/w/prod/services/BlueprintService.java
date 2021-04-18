package com.w.prod.services;

import com.w.prod.models.service.BlueprintLogServiceModel;
import com.w.prod.models.service.BlueprintServiceModel;
import com.w.prod.models.view.BlueprintViewModel;

import java.util.List;

public interface BlueprintService {

    BlueprintViewModel createBlueprint(BlueprintServiceModel blueprintServiceModel);

    List<BlueprintViewModel> getAll();

    BlueprintServiceModel extractBlueprintModel(String id);

    List<String> deleteBlueprint(String id);

    boolean markBlueprintAsAccepted(String id);

    int getDurationOfBlueprint(String id);

    BlueprintViewModel getBlueprintView(String id);

    List<String> deleteBlueprintsOfUser(String id);

    BlueprintLogServiceModel generateBlueprintServiceModel(String blueprintName);
}
