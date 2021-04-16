package com.w.prod.web;

import com.w.prod.model.binding.ProductAddBindingModel;
import com.w.prod.model.service.ProductServiceModel;
import com.w.prod.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public String add(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            return "redirect:/users/login";
        }

        if (!model.containsAttribute("productAddBindingModel")) {
            model.addAttribute("productAddBindingModel", new ProductAddBindingModel());
        }
        return "add-product";
    }

    @PostMapping("/add")
    public String addConfirm(
            @Valid ProductAddBindingModel productAddBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel", bindingResult);

            return "redirect:add";
        }

        productService
                .add(modelMapper.map(productAddBindingModel, ProductServiceModel.class));

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete (@PathVariable String id) {
        productService.deleteById(id);
        return "redirect:/";
    }
}
