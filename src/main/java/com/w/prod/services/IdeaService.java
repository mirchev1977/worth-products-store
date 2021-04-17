package com.w.prod.services;

import com.w.prod.models.service.IdeaLogServiceModel;
import com.w.prod.models.service.IdeaServiceModel;
import com.w.prod.models.view.IdeaViewModel;

import java.util.List;

public interface IdeaService {

    IdeaViewModel createIdea(IdeaServiceModel ideaServiceModel);

    List<IdeaViewModel> getAll();

    IdeaServiceModel extractIdeaModel(String id);

    List<String> deleteIdea(String id);

    boolean markIdeaAsAccepted(String id);

    int getDurationOfIdea(String id);

    IdeaViewModel getIdeaView(String id);

    List<String> deleteIdeasOfUser(String id);

    IdeaLogServiceModel generateIdeaServiceModel(String ideaName);
}
