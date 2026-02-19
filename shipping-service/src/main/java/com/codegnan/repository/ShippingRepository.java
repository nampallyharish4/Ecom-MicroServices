package com.codegnan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codegnan.model.ShippingInfo;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingInfo,Long>{

	ShippingInfo findByOrderId(Long orderId);
}
