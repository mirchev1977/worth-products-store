package com.w.prod.web;

import com.w.prod.models.view.ProductResultViewModel;
import com.w.prod.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/results/")
public class ResultsController {

    private final ModelMapper modelMapper;
    private final ProductService productService;

    public ResultsController(ModelMapper modelMapper, ProductService productService) {
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @GetMapping("/it")
    public String resultsIT(Model model) {
        List<ProductResultViewModel> productResultViewModels = productService.getResults("IT");
        model.addAttribute("results", productResultViewModels);
        return "results-it";
    }

    @GetMapping("/arts")
    public String resultsArts(Model model) {
        List<ProductResultViewModel> productResultViewModels = productService.getResults("Arts");
        model.addAttribute("results", productResultViewModels);
        return "results-art";
    }
    @GetMapping("/production")
    public String resultsProduction(Model model) {
        List<ProductResultViewModel> productResultViewModels = productService.getResults("Production");
        model.addAttribute("results", productResultViewModels);
        return "results-production";
    }
    @GetMapping("/education")
    public String resultsEducation(Model model) {
        List<ProductResultViewModel> productResultViewModels = productService.getResults("Education");
        model.addAttribute("results", productResultViewModels);
        return "results-edu";
    }
}
