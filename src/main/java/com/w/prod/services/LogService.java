package com.w.prod.services;

import com.w.prod.models.view.AddIdeaLogViewModel;
import com.w.prod.models.view.JoinProductLogViewModel;

import java.util.List;
import java.util.Map;

public interface LogService {

    void createProductJoinLog(String action, String productId);

    List<JoinProductLogViewModel> findAllJoinProductLogs();

    void createIdeaAddLog(String action, String ideaId);

    List<AddIdeaLogViewModel>  findAllIdeaAddLogs();

    Map<Integer, Integer> getStatsIdeasCreated();

    Map<Integer, Integer> getStatsJoinProductActivity();


}
