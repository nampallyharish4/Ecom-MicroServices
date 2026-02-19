package com.codegnan.service;

import java.util.List;

import com.codegnan.dto.FavouriteRequestDTO;
import com.codegnan.dto.FavouriteResponseDTO;

public interface FavouriteService {

    FavouriteResponseDTO addFavourite(FavouriteRequestDTO request);

    void removeFavourite(Integer userId, Long productId);

    List<FavouriteResponseDTO> getUserFavourites(Integer userId);
}

