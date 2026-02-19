package com.codegnan.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.codegnan.dto.FavouriteRequestDTO;
import com.codegnan.dto.FavouriteResponseDTO;
import com.codegnan.service.FavouriteService;

@RestController
@RequestMapping("/favourites")
public class FavouriteController {

    private static final Logger logger =
            LoggerFactory.getLogger(FavouriteController.class);

    @Autowired
    private FavouriteService favouriteService;

    @PostMapping
    public FavouriteResponseDTO addFavourite(
            @RequestBody FavouriteRequestDTO request) {

        logger.info("FavouriteController :: addFavourite() started");
        logger.debug("Request details - userId: {}, productId: {}",
                request.getUserId(), request.getProductId());

        FavouriteResponseDTO response =
                favouriteService.addFavourite(request);

        logger.info("FavouriteController :: addFavourite() completed successfully with id: {}",
                response.getId());

        return response;
    }

    @DeleteMapping
    public String removeFavourite(
            @RequestParam Integer userId,
            @RequestParam Long productId) {

        logger.info("FavouriteController :: removeFavourite() started");
        logger.debug("Request details - userId: {}, productId: {}",
                userId, productId);

        favouriteService.removeFavourite(userId, productId);

        logger.info("FavouriteController :: removeFavourite() completed successfully");

        return "Removed Successfully";
    }

    @GetMapping("/{userId}")
    public List<FavouriteResponseDTO> getUserFavourites(
            @PathVariable Integer userId) {

        logger.info("FavouriteController :: getUserFavourites() started for userId: {}",
                userId);

        List<FavouriteResponseDTO> favourites =
                favouriteService.getUserFavourites(userId);

        logger.info("FavouriteController :: getUserFavourites() completed. Total records: {}",
                favourites.size());

        return favourites;
    }
}
