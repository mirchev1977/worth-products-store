package com.w.prod.web;

import com.w.prod.models.view.ProductBasicViewModel;
import com.w.prod.services.ProductService;
import com.w.prod.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/manageProducts")
@RestController
public class ProductRestController {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public ProductRestController(ProductService productService, ModelMapper modelMapper, UserService userService) {
        this.productService = productService;

        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/api/all")
    public ResponseEntity<List<ProductBasicViewModel>> findAll() {
        return ResponseEntity
                .ok()
                .body(productService.getActiveProductsOrderedbyStartDate());
    }

    @GetMapping("/own")
    public ResponseEntity<List<ProductBasicViewModel>> showMyOwnProducts(@AuthenticationPrincipal UserDetails principal) {
        String userName = principal.getUsername();
        return ResponseEntity
                .ok()
                .body(
                        productService.getUserProductsOrderedByStartDate(userName));
    }

    @GetMapping("/collaborations")
    public ResponseEntity<List<ProductBasicViewModel>> showMyCollaborations(@AuthenticationPrincipal UserDetails principal) {
        String userName = principal.getUsername();
        return ResponseEntity
                .ok()
                .body(
                        productService.getUserCollaborationsOrderedByStartDate(userName));
    }
}
