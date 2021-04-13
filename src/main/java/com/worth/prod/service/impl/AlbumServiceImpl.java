package com.worth.prod.service.impl;

import com.worth.prod.model.entity.AlbumEntity;
import com.worth.prod.model.entity.UserEntity;
import com.worth.prod.model.entity.enums.ArtistName;
import com.worth.prod.model.service.AlbumServiceModel;
import com.worth.prod.model.view.AlbumViewModel;
import com.worth.prod.repository.AlbumRepository;
import com.worth.prod.service.AlbumService;
import com.worth.prod.service.ArtistService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistService artistService;
    private final ModelMapper modelMapper;
    private final HttpSession httpSession;

    public AlbumServiceImpl(AlbumRepository albumRepository, ArtistService artistService, ModelMapper modelMapper, HttpSession httpSession) {
        this.albumRepository = albumRepository;
        this.artistService = artistService;
        this.modelMapper = modelMapper;
        this.httpSession = httpSession;
    }

    @Override
    public void add(AlbumServiceModel albumServiceModel) {
        AlbumEntity albumEntity = modelMapper.map(albumServiceModel, AlbumEntity.class);
        albumEntity.setArtistEntity(artistService.findByName(albumServiceModel.getArtist()));
        albumEntity.setAddedFrom(modelMapper.map(httpSession.getAttribute("user"),
                UserEntity.class
                ));

        albumRepository.save(albumEntity);
    }

    @Override
    public BigDecimal getTotalSum() {
//        return albumRepository.findTotalAlbumsSum();
        return null;
    }

    @Override
    public List<AlbumViewModel> findAllAlbumsByArtistName(ArtistName artistName) {
//        return albumRepository.findAllByArtist_Name(artistName)
//                .stream().map(albumEntity -> modelMapper.map(albumEntity, AlbumViewModel.class))
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public void buyById(String id) {
        albumRepository.deleteById(id);
    }

    @Override
    public void buyAll() {
        albumRepository.deleteAll();
    }

    @Override
    public List<AlbumViewModel> getAllByUser(UserEntity user) {
        return albumRepository.findAllByAddedFromOrderByCopiesDesc(user).stream().map(albumEntity ->
                modelMapper.map(albumEntity, AlbumViewModel.class)).collect(Collectors.toList());
    }

    @Override
    public int getTotalAlbumsSold(UserEntity user) {
        return albumRepository.findAllByAddedFrom(user);
    }

    @Override
    public void deleteById(String id) {
        albumRepository.deleteById(id);
    }
}
