package com.w.prod.web;

import com.w.prod.models.binding.UserRegistrationBindingModel;
import com.w.prod.models.service.UserRegistrationServiceModel;
import com.w.prod.services.BlueprintService;
import com.w.prod.services.ProductService;
import com.w.prod.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ProductService productService;
    private final BlueprintService blueprintService;

    public UserController(ModelMapper modelMapper, UserService userService, ProductService productService, BlueprintService blueprintService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.productService = productService;
        this.blueprintService = blueprintService;
    }

    @ModelAttribute("userRegistrationBindingModel")
    public UserRegistrationBindingModel userRegistrationBindingModel() {
        return new UserRegistrationBindingModel();
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/expired")
    public String sessionExpired() {
        return "session-expired";
    }

    @PostMapping("/login-error")
    public ModelAndView failedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        redirectAttributes.addFlashAttribute("bad_credentials", true);
        redirectAttributes.addFlashAttribute("username", username);

        modelAndView.setViewName("redirect:/users/login");
        return modelAndView;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerAndLoginUser(
            @Valid UserRegistrationBindingModel userRegistrationBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationBindingModel", userRegistrationBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationBindingModel", bindingResult);
            return "redirect:/users/register";
        }
        if (userService.usernameExists(userRegistrationBindingModel.getUsername())) {
            redirectAttributes.addFlashAttribute("userRegistrationBindingModel", userRegistrationBindingModel);
            redirectAttributes.addFlashAttribute("userExistsError", true);
            return "redirect:/users/register";

        }

        UserRegistrationServiceModel userRegistrationServiceModel = modelMapper.map(userRegistrationBindingModel, UserRegistrationServiceModel.class);
        userService.registerAndLoginUser(userRegistrationServiceModel);
        return "redirect:/home";
    }


    @GetMapping("/manage")
    public String manage(Model model) {
       model.addAttribute("users", userService.getAll());
        return "users-all";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        blueprintService.deleteBlueprintsOfUser(id);
        productService.deleteProductsOfUser(id);
        userService.deleteUser(id);
        return "redirect:/users/manage";
    }
}
