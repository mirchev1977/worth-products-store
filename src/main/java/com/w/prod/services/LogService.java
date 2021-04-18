package com.w.prod.services;

import com.w.prod.models.view.AddBlueprintLogViewModel;
import com.w.prod.models.view.JoinProductLogViewModel;

import java.util.List;
import java.util.Map;

public interface LogService {

    void createProductJoinLog(String action, String productId);

    List<JoinProductLogViewModel> findAllJoinProductLogs();

    void createBlueprintAddLog(String action, String blueprintId);

    List<AddBlueprintLogViewModel>  findAllBlueprintAddLogs();

    Map<Integer, Integer> getStatsBlueprintsCreated();

    Map<Integer, Integer> getStatsJoinProductActivity();


}
