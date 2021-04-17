package com.w.prod.services;

import com.w.prod.models.view.AddIdeaLogViewModel;
import com.w.prod.models.view.JoinProjectLogViewModel;

import java.util.List;
import java.util.Map;

public interface LogService {

    void createProjectJoinLog(String action, String projectId);

    List<JoinProjectLogViewModel> findAllJoinProjectLogs();

    void createIdeaAddLog(String action, String ideaId);

    List<AddIdeaLogViewModel>  findAllIdeaAddLogs();

    Map<Integer, Integer> getStatsIdeasCreated();

    Map<Integer, Integer> getStatsJoinProjectActivity();


}
