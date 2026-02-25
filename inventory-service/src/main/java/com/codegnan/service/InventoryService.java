package com.codegnan.service;

import com.codegnan.model.Inventory;
import com.codegnan.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .map(inventory -> inventory.getQuantity() > 0)
                .orElse(false);
    }

    public void addStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElse(new Inventory(null, productId, 0));
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    public boolean deductStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElse(null);
        if (inventory != null && inventory.getQuantity() >= quantity) {
            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventoryRepository.save(inventory);
            return true;
        }
        return false;
    }
}
