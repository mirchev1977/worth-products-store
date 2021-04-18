package com.w.prod.web;

import com.w.prod.models.binding.BlueprintAddBindingModel;
import com.w.prod.models.binding.ProductAddBindingModel;
import com.w.prod.models.service.BlueprintServiceModel;
import com.w.prod.models.service.ProductServiceModel;
import com.w.prod.models.view.BlueprintViewModel;
import com.w.prod.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/blueprints")
public class BlueprintController {
    private final CarouselService carouselService;
    private final ModelMapper modelMapper;
    private final BlueprintService blueprintService;
    private final ActivityTypeService activityTypeService;
    private final EquipmentService equipmentService;
    private final ProductService productService;
    private final PremiseService premiseService;
    private final LogService logService;

    public BlueprintController(CarouselService carouselService, ModelMapper modelMapper, BlueprintService blueprintService, ActivityTypeService activityTypeService, EquipmentService equipmentService, ProductService productService, PremiseService premiseService, LogService logService) {
        this.carouselService = carouselService;
        this.modelMapper = modelMapper;
        this.blueprintService = blueprintService;
        this.activityTypeService = activityTypeService;
        this.equipmentService = equipmentService;
        this.productService = productService;
        this.premiseService = premiseService;
        this.logService = logService;
    }

    @ModelAttribute("blueprintAddBindingModel")
    public BlueprintAddBindingModel createBindingModel() {
        return new BlueprintAddBindingModel();
    }

    @ModelAttribute("activityTypes")
    public List<String> activityTypes() {
        return activityTypeService.getAllActivities();
    }

    @ModelAttribute("equipmentTypes")
    public List<String> equipmentTypes() {
        return equipmentService.getAllEquipments();
    }

    @ModelAttribute("productAddBindingModel")
    public ProductAddBindingModel productAddBindingModel() {
        return new ProductAddBindingModel();
    }

    @ModelAttribute("message")
    public String message() {
        return "";
    }


    @GetMapping("/all")
    public String showAll(Model model) {
        model.addAttribute("blueprints", blueprintService.getAll());

        return "blueprint-all";
    }

    @GetMapping("/add")
    public String addBlueprint(Model model) {

        model.addAttribute("firstImg", carouselService.firstImage());
        model.addAttribute("secondImg", carouselService.secondImage());
        model.addAttribute("thirdImg", carouselService.thirdImage());

        return "blueprint-add";
    }

    @PostMapping("/add")
    public String postBlueprint(@Valid BlueprintAddBindingModel blueprintAddBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {

            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("blueprintAddBindingModel", blueprintAddBindingModel);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.blueprintAddBindingModel", bindingResult);
                return "redirect:/blueprints/add";
            }

            BlueprintServiceModel blueprintServiceModel = modelMapper.map(
                    blueprintAddBindingModel,
                    BlueprintServiceModel.class);

            blueprintServiceModel.setPromoter(principal.getUsername());

            blueprintService.createBlueprint(blueprintServiceModel);
            redirectAttributes.addFlashAttribute("message", "Your blueprint was added");
        }
        return "redirect:/blueprints/all";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable String id,
                              Model model) {

        BlueprintViewModel blueprintViewModel = blueprintService.getBlueprintView(id);
        model.addAttribute("current", blueprintViewModel);

        return "blueprint-details";
    }

    @GetMapping("/accept/{id}")
    public String acceptBlueprint(@PathVariable String id, Model model) {
        BlueprintServiceModel blueprintServiceModel = blueprintService.extractBlueprintModel(id);
        model.addAttribute("blueprintServiceModel", blueprintServiceModel);
        model.addAttribute("premises", premiseService.findSuitablePremises(blueprintServiceModel.getNeededEquipment()));
        model.addAttribute("duration", blueprintServiceModel.getDuration());
        model.addAttribute("premisesInfo", premiseService.getSuitablePremisesWithProducts(blueprintServiceModel.getNeededEquipment()));

        return "product-add";
    }

    @PostMapping("/accept/{id}")
    public String turnBlueprintIntoProduct(@PathVariable String id,
                                      @Valid ProductAddBindingModel productAddBindingModel,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        model.addAttribute("duration", blueprintService.getDurationOfBlueprint(id));
        model.addAttribute("id", id);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel", bindingResult);
            return "redirect:/blueprints/accept/{id}";
        }

        ProductServiceModel productServiceModel = modelMapper.map(
                productAddBindingModel, ProductServiceModel.class);
        productServiceModel.setPromoter(blueprintService.extractBlueprintModel(id).getPromoter());
        productService.createProduct(productServiceModel);

        blueprintService.markBlueprintAsAccepted(id);
        redirectAttributes.addFlashAttribute("message", "You created a product");

        return "redirect:/blueprints/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlueprint(@PathVariable String id,
                             RedirectAttributes redirectAttributes
    ) {
        blueprintService.deleteBlueprint(id);
        redirectAttributes.addFlashAttribute("message", "You deleted an blueprint");

        return "redirect:/blueprints/all";
    }
}
