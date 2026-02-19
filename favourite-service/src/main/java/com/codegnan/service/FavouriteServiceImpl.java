package com.codegnan.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.codegnan.dto.FavouriteRequestDTO;
import com.codegnan.dto.FavouriteResponseDTO;
import com.codegnan.exceptions.ResourceNotFoundException;
import com.codegnan.feignclient.ProductFeignClient;
import com.codegnan.feignclient.UserFeignClient;
import com.codegnan.model.Favourite;
import com.codegnan.repository.FavouriteRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class FavouriteServiceImpl implements FavouriteService {

	private FavouriteRepository favouriteRepository;
	private UserFeignClient userFeignClient;
	private ProductFeignClient productFeignClient;

	@Override
	public FavouriteResponseDTO addFavourite(FavouriteRequestDTO request) {

		log.info("FavouriteServiceImpl :: addFavourite() started");
		log.debug("UserId: {}, ProductId: {}", request.getUserId(), request.getProductId());

		// Validate user
		log.debug("Validating user via USER-SERVICE");
		if (userFeignClient.fetchUser(request.getUserId().intValue()) == null) {
			log.error("User not found with userId: {}", request.getUserId());
			throw new ResourceNotFoundException("User Not Found");
		}

		// Validate product
		log.debug("Validating product via PRODUCT-SERVICE");
		if (productFeignClient.getProduct(request.getProductId()) == null) {
			log.error("Product not found with productId: {}", request.getProductId());
			throw new ResourceNotFoundException("Product Not Found");
		}

		// Check duplicate
		log.debug("Checking if product already exists in favourites");
		Optional<Favourite> existing =
				favouriteRepository.findByUserIdAndProductId(
						request.getUserId(),
						request.getProductId());

		if (existing.isPresent()) {
			log.warn("Product already in favourites for userId: {}, productId: {}",
					request.getUserId(), request.getProductId());
			throw new ResourceNotFoundException("Product already in favourites");
		}

		Favourite favourite = new Favourite();
		favourite.setUserId(request.getUserId());
		favourite.setProductId(request.getProductId());
		favourite.setAddedAt(LocalDateTime.now());

		log.debug("Saving favourite to database");
		Favourite saved = favouriteRepository.save(favourite);

		log.info("Favourite saved successfully with id: {}", saved.getId());

		FavouriteResponseDTO response = new FavouriteResponseDTO();
		BeanUtils.copyProperties(saved, response);

		log.info("FavouriteServiceImpl :: addFavourite() completed");

		return response;
	}

	@Override
	public void removeFavourite(Integer userId, Long productId) {

		log.info("FavouriteServiceImpl :: removeFavourite() called");
		log.debug("UserId: {}, ProductId: {}", userId, productId);

		Favourite fav = favouriteRepository
				.findByUserIdAndProductId(userId, productId)
				.orElseThrow(() -> {
					log.error("Favourite not found for userId: {}, productId: {}",
							userId, productId);
					return new ResourceNotFoundException("Not Found");
				});

		favouriteRepository.delete(fav);

		log.info("Favourite removed successfully for userId: {}, productId: {}",
				userId, productId);
	}

	@Override
	public List<FavouriteResponseDTO> getUserFavourites(Integer userId) {

		log.info("FavouriteServiceImpl :: getUserFavourites() called for userId: {}", userId);

		List<FavouriteResponseDTO> list =
				favouriteRepository.findByUserId(userId)
				.stream()
				.map(fav -> {
					FavouriteResponseDTO dto = new FavouriteResponseDTO();
					BeanUtils.copyProperties(fav, dto);
					return dto;
				})
				.toList();

		log.info("Total favourites found: {} for userId: {}", list.size(), userId);

		return list;
	}
}
