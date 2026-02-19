package com.codegnan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codegnan.model.Favourite;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

	List<Favourite> findByUserId(Integer userId);

	Optional<Favourite> findByUserIdAndProductId(Integer userId, Long productId);
}
