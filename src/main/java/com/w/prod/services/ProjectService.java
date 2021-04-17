package com.w.prod.services;

import com.w.prod.models.service.ProjectResultServiceModel;
import com.w.prod.models.service.ProjectServiceModel;
import com.w.prod.models.view.ProjectBasicViewModel;
import com.w.prod.models.view.ProjectDetailedViewModel;
import com.w.prod.models.view.ProjectResultViewModel;

import java.util.List;

public interface ProjectService {
    ProjectBasicViewModel createProject(ProjectServiceModel projectServiceModel);

    List<ProjectBasicViewModel> getActiveProjectsOrderedbyStartDate();

    ProjectDetailedViewModel extractProjectModel(String id);

    List<String> deleteProject(String id);

    List<ProjectBasicViewModel> getUserProjectsOrderedByStartDate(String username);

    void archiveProject(String id);

    String findProjectOwnerStr(String id);

    boolean joinProject(String id, String userName);

    boolean checkIfCollaborating(String id, String userName);

    void leaveProject(String id, String userName);

    ProjectServiceModel extractProjectServiceModel(String id);

    String getProjectPromoter(String id);

    void updateProject(String id, ProjectServiceModel projectServiceModel);

    void publishProjectResult(ProjectResultServiceModel projectServiceModel);

    ProjectResultServiceModel extractProjectResultServiceModel(String id);

    List<ProjectResultViewModel> getResults(String it);

    List<ProjectBasicViewModel> getUserCollaborationsOrderedByStartDate(String userName);

    long getDurationInDays(ProjectServiceModel projectAddBindingModel);

    void deleteProjectsOfUser(String id);

    ProjectServiceModel findProjectById(String projectId);
}
