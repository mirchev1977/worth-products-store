package com.worth.prod.web;

import com.worth.prod.model.entity.UserEntity;
import com.worth.prod.model.view.AlbumViewModel;
import com.worth.prod.repository.AlbumRepository;
import com.worth.prod.service.AlbumService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {
    private final AlbumService albumService;
    private final ModelMapper modelMapper;
    private final AlbumRepository albumRepository;

    public HomeController(AlbumService albumService, ModelMapper modelMapper, AlbumRepository albumRepository) {
        this.albumService = albumService;
        this.modelMapper = modelMapper;
        this.albumRepository = albumRepository;
    }

    @GetMapping("/")
    public String index(HttpSession httpSession, Model model) {
        if (httpSession.getAttribute("user") == null) {
            return "index";
        }

        if (albumRepository.count() == 0) {
            return "index";
        }

        UserEntity user = modelMapper.map(httpSession.getAttribute("user"),
                UserEntity.class);

        List<AlbumViewModel> albumViewModels = albumService.getAllByUser(user);
        int totalAlbumsSold = albumService.getTotalAlbumsSold(user);

        model.addAttribute("albumViewModels", albumViewModels);
        model.addAttribute("totalAlbumsSold", totalAlbumsSold);


        return "home";
    }
}
