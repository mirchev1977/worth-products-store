package com.w.prod.web;

import com.w.prod.services.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
public class StatsController {
    private final LogService logService;

    public StatsController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public String stats(Model model) {
        model.addAttribute("jointProductLogs", logService.findAllJoinProductLogs());
        model.addAttribute("addIdeaLogs", logService.findAllIdeaAddLogs());
        model.addAttribute("activityIdeas", logService.getStatsIdeasCreated());
        model.addAttribute("activityProducts", logService.getStatsJoinProductActivity());

        return "stats";
    }
}


