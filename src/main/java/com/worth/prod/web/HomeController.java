package com.worth.prod.web;

import com.worth.prod.model.entity.UserEntity;
import com.worth.prod.model.view.ProductViewModel;
import com.worth.prod.repository.ProductRepository;
import com.worth.prod.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public HomeController(ProductService productService, ModelMapper modelMapper, ProductRepository productRepository) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String index(HttpSession httpSession, Model model) {
        if (httpSession.getAttribute("user") == null) {
            return "index";
        }

        if (productRepository.count() == 0) {
            return "index";
        }

        UserEntity user = modelMapper.map(httpSession.getAttribute("user"),
                UserEntity.class);

        List<ProductViewModel> productViewModels = productService.getAllByUser(user);
        int totalProductsSold = productService.getTotalProductsSold(user);

        model.addAttribute("productViewModels", productViewModels);
        model.addAttribute("totalProductsSold", totalProductsSold);


        return "home";
    }
}
