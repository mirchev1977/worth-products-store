package com.w.prod.web;

import com.w.prod.models.view.ProjectBasicViewModel;
import com.w.prod.services.ProjectService;
import com.w.prod.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/manageProjects")
@RestController
public class ProjectRestController {

    private final ProjectService projectService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public ProjectRestController(ProjectService projectService, ModelMapper modelMapper, UserService userService) {
        this.projectService = projectService;

        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/api/all")
    public ResponseEntity<List<ProjectBasicViewModel>> findAll() {
        return ResponseEntity
                .ok()
                .body(projectService.getActiveProjectsOrderedbyStartDate());
    }

    @GetMapping("/own")
    public ResponseEntity<List<ProjectBasicViewModel>> showMyOwnProjects(@AuthenticationPrincipal UserDetails principal) {
        String userName = principal.getUsername();
        return ResponseEntity
                .ok()
                .body(
                        projectService.getUserProjectsOrderedByStartDate(userName));
    }

    @GetMapping("/collaborations")
    public ResponseEntity<List<ProjectBasicViewModel>> showMyCollaborations(@AuthenticationPrincipal UserDetails principal) {
        String userName = principal.getUsername();
        return ResponseEntity
                .ok()
                .body(
                        projectService.getUserCollaborationsOrderedByStartDate(userName));
    }
}
