package com.w.prod.web;

import com.w.prod.models.binding.IdeaAddBindingModel;
import com.w.prod.models.binding.ProductAddBindingModel;
import com.w.prod.models.service.IdeaServiceModel;
import com.w.prod.models.service.ProductServiceModel;
import com.w.prod.models.view.IdeaViewModel;
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
@RequestMapping("/ideas")
public class IdeaController {
    private final CarouselService carouselService;
    private final ModelMapper modelMapper;
    private final IdeaService ideaService;
    private final ActivityTypeService activityTypeService;
    private final EquipmentService equipmentService;
    private final ProductService productService;
    private final LabService labService;
    private final LogService logService;

    public IdeaController(CarouselService carouselService, ModelMapper modelMapper, IdeaService ideaService, ActivityTypeService activityTypeService, EquipmentService equipmentService, ProductService productService, LabService labService, LogService logService) {
        this.carouselService = carouselService;
        this.modelMapper = modelMapper;
        this.ideaService = ideaService;
        this.activityTypeService = activityTypeService;
        this.equipmentService = equipmentService;
        this.productService = productService;
        this.labService = labService;
        this.logService = logService;
    }

    @ModelAttribute("ideaAddBindingModel")
    public IdeaAddBindingModel createBindingModel() {
        return new IdeaAddBindingModel();
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
        model.addAttribute("ideas", ideaService.getAll());

        return "ideas-all";
    }

    @GetMapping("/add")
    public String addIdea(Model model) {

        model.addAttribute("firstImg", carouselService.firstImage());
        model.addAttribute("secondImg", carouselService.secondImage());
        model.addAttribute("thirdImg", carouselService.thirdImage());

        return "idea-add";
    }

    @PostMapping("/add")
    public String postIdea(@Valid IdeaAddBindingModel ideaAddBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {

            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("ideaAddBindingModel", ideaAddBindingModel);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ideaAddBindingModel", bindingResult);
                return "redirect:/ideas/add";
            }

            IdeaServiceModel ideaServiceModel = modelMapper.map(
                    ideaAddBindingModel,
                    IdeaServiceModel.class);

            ideaServiceModel.setPromoter(principal.getUsername());

            ideaService.createIdea(ideaServiceModel);
            redirectAttributes.addFlashAttribute("message", "Your idea was added");
        }
        return "redirect:/ideas/all";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable String id,
                              Model model) {

        IdeaViewModel ideaViewModel = ideaService.getIdeaView(id);
        model.addAttribute("current", ideaViewModel);

        return "idea-details";
    }

    @GetMapping("/accept/{id}")
    public String acceptIdea(@PathVariable String id, Model model) {
        IdeaServiceModel ideaServiceModel = ideaService.extractIdeaModel(id);
        model.addAttribute("ideaServiceModel", ideaServiceModel);
        model.addAttribute("labs", labService.findSuitableLabs(ideaServiceModel.getNeededEquipment()));
        model.addAttribute("duration", ideaServiceModel.getDuration());
        model.addAttribute("labsInfo", labService.getSuitableLabsWithProducts(ideaServiceModel.getNeededEquipment()));

        return "product-add";
    }

    @PostMapping("/accept/{id}")
    public String turnIdeaIntoProduct(@PathVariable String id,
                                      @Valid ProductAddBindingModel productAddBindingModel,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        model.addAttribute("duration", ideaService.getDurationOfIdea(id));
        model.addAttribute("id", id);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel", bindingResult);
            return "redirect:/ideas/accept/{id}";
        }

        ProductServiceModel productServiceModel = modelMapper.map(
                productAddBindingModel, ProductServiceModel.class);
        productServiceModel.setPromoter(ideaService.extractIdeaModel(id).getPromoter());
        productService.createProduct(productServiceModel);

        ideaService.markIdeaAsAccepted(id);
        redirectAttributes.addFlashAttribute("message", "You created a product");

        return "redirect:/ideas/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteIdea(@PathVariable String id,
                             RedirectAttributes redirectAttributes
    ) {
        ideaService.deleteIdea(id);
        redirectAttributes.addFlashAttribute("message", "You deleted an idea");

        return "redirect:/ideas/all";
    }
}
