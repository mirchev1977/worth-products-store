package com.w.prod.web;

import com.w.prod.models.binding.ProductAddBindingModel;
import com.w.prod.models.binding.ProductResultBindingModel;
import com.w.prod.models.service.ProductResultServiceModel;
import com.w.prod.models.service.ProductServiceModel;
import com.w.prod.models.view.ProductBasicViewModel;
import com.w.prod.models.view.ProductDetailedViewModel;
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
@RequestMapping("/products")
public class ProductController {
    private final ModelMapper modelMapper;
    private final ActivityTypeService activityTypeService;
    private final EquipmentService equipmentService;
    private final ProductService productService;
    private final PremiseService premiseService;
    private final UserService userService;
    private final LogService logService;

    public ProductController(ModelMapper modelMapper, ActivityTypeService activityTypeService, EquipmentService equipmentService, ProductService productService, PremiseService premiseService, UserService userService, LogService logService) {
        this.modelMapper = modelMapper;
        this.activityTypeService = activityTypeService;
        this.equipmentService = equipmentService;
        this.productService = productService;
        this.premiseService = premiseService;
        this.userService = userService;
        this.logService = logService;
    }

    @ModelAttribute("productAddBindingModel")
    public ProductAddBindingModel productAddBindingModel() {
        return new ProductAddBindingModel();
    }

    @ModelAttribute("productResultBindingModel")
    public ProductResultBindingModel productResultBindingModel() {
        return new ProductResultBindingModel();
    }

    @ModelAttribute("activityTypes")
    public List<String> activityTypes() {
        return activityTypeService.getAllActivities();
    }

    @ModelAttribute("equipmentTypes")
    public List<String> equipmentTypes() {
        return equipmentService.getAllEquipments();
    }

    @ModelAttribute("message")
    public String message() {
        return "";
    }

    @GetMapping("/all")
    public String showAll(Model model) {
        List<ProductBasicViewModel> productViewModels =  productService.getActiveProductsOrderedbyStartDate();

        model.addAttribute("productViewModels", productViewModels);

        return "products-all";
    }

    @GetMapping("/owned")
    public String showOwnProducts(Model model,
                                  @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {
            String firstName = (userService.findByUsername(principal.getUsername())).getFirstName();
            String lastName = (userService.findByUsername(principal.getUsername())).getLastName();
            String userName = (userService.findByUsername(principal.getUsername())).getUsername();
            String fullName = String.format("%s %s", firstName, lastName);
            model.addAttribute("fullName", fullName);
            model.addAttribute("productsOfUser", productService.getUserProductsOrderedByStartDate(userName));
        }
        return "products-own";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable String id,
                              Model model,
                              @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {
            String userName = principal.getUsername();
            String owner = productService.findProductOwnerStr(id);
            boolean isOwner = userName.equals(owner);
            model.addAttribute("isCollaborating", productService.checkIfCollaborating(id, userName));
            ProductDetailedViewModel productViewModel = productService.extractProductModel(id);
            model.addAttribute("current", productViewModel);
            model.addAttribute("isOwner", isOwner);
        }
        return "product-details";
    }

    @GetMapping("/archive/{id}")
    public String archiveProduct(@PathVariable String id,
                                 RedirectAttributes redirectAttributes) {
        productService.archiveProduct(id);
        redirectAttributes.addFlashAttribute("message", "You archived a product");

        return "redirect:/products/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id,
                                RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "You deleted a product");

        return "redirect:/products/all";
    }

    @GetMapping("/join/{id}")
    public String joinProduct(@PathVariable String id,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {
            String userName = principal.getUsername();
            productService.joinProduct(id, userName);

            redirectAttributes.addFlashAttribute("message", "You are now a collaborator in the product");
        }
        return "redirect:/products/details/{id}";
    }

    @GetMapping("/leave/{id}")
    public String leaveProduct(@PathVariable String id,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {
            String userName = principal.getUsername();
            productService.leaveProduct(id, userName);
            model.addAttribute("id", id);
            redirectAttributes.addFlashAttribute("message", "You are no longer a collaborator in the product");
        }
        return "redirect:/products/details/{id}";

    }

    @GetMapping("/update/{id}")
    public String updateProduct(@PathVariable String id,
                                Model model) {
        ProductServiceModel currentData = productService.extractProductServiceModel(id);
        long durationInDays = productService.getDurationInDays(currentData);
        model.addAttribute("current", currentData);
        model.addAttribute("premises", premiseService.getAllPremises());
        model.addAttribute("duration", durationInDays);
        model.addAttribute("premisesInfo", premiseService.getAllPremisesWithProducts());

        return "product-update";
    }


    @PostMapping("/update/{id}")
    public String updateProductPost(@PathVariable String id,
                                    @Valid ProductAddBindingModel productAddBindingModel,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        model.addAttribute("id", id);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel", bindingResult);
            return "redirect:/products/update/{id}";
        }

        ProductServiceModel productServiceModel = modelMapper.map(
                productAddBindingModel, ProductServiceModel.class);
        productServiceModel.setPromoter(productService.getProductPromoter(id));
        productService.updateProduct(id, productServiceModel);

        redirectAttributes.addFlashAttribute("message", "You updated the product");
        return "redirect:/products/details/{id}";
    }

    @GetMapping("/publish/{id}")
    public String publishProduct(@PathVariable String id,
                                 Model model) {
        ProductResultServiceModel productResultServiceModel = productService.extractProductResultServiceModel(id);
        model.addAttribute("current", productResultServiceModel);
        model.addAttribute("id", id);
        return "product-publish";
    }


    @PostMapping("/publish/{id}")
    public String publishProductPost(@PathVariable String id,
                                     @Valid ProductResultBindingModel productResultBindingModel,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productResultBindingModel", productResultBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productResultBindingModel", bindingResult);
            return "redirect:/products/publish/{id}";
        }

        ProductResultServiceModel productResultServiceModel = modelMapper.map(
                productResultBindingModel, ProductResultServiceModel.class);
        productResultServiceModel.setId(id);
        productService.publishProductResult(productResultServiceModel);

        redirectAttributes.addFlashAttribute("message", "You published the results from the product");
        return "redirect:/products/details/{id}";
    }



}
