package com.codegnan.controller;

import com.codegnan.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Boolean> isInStock(@RequestParam Long productId) {
        return ResponseEntity.ok(inventoryService.isInStock(productId));
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addStock(@RequestParam Long productId, @RequestParam Integer quantity) {
        inventoryService.addStock(productId, quantity);
        return ResponseEntity.ok("Stock added successfully.");
    }

    @RequestMapping(path = "/deduct", method = RequestMethod.POST)
    public ResponseEntity<String> deductStock(@RequestParam Long productId, @RequestParam Integer quantity) {
        if (inventoryService.deductStock(productId, quantity)) {
            return ResponseEntity.ok("Stock deducted successfully.");
        }
        return ResponseEntity.badRequest().body("Insufficient stock.");
    }
}
