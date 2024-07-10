package com.zion.controller;

import com.zion.bean.IngredientPrice;
import com.zion.controller.api.IngredientPriceControllerIFC;
import com.zion.service.IngredientPriceService;
import com.zion.service.exceptions.IngredientPriceInUseException;
import com.zion.service.exceptions.IngredientPriceNotFoundException;
import com.zion.service.exceptions.IngredientPriceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/ingredientPrices")
public class IngredientPriceController implements IngredientPriceControllerIFC {

    @Autowired
    private IngredientPriceService ingredientPriceService;

    @GetMapping
    public ResponseEntity<?> getAllIngredientPrices() {
        try {
            List<IngredientPrice> ingredientPrices = ingredientPriceService.getAllIngredientPrices();
            return ResponseEntity.ok(ingredientPrices);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve ingredient prices: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createIngredientPrice(@RequestBody IngredientPrice ingredientPrice) {
        try {
            IngredientPrice createdIngredientPrice = ingredientPriceService.addIngredientPrice(ingredientPrice);
            return ResponseEntity.ok(createdIngredientPrice);
        } catch (IngredientPriceValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateIngredientPrice(@PathVariable Long id, @RequestBody IngredientPrice updatedIngredientPrice) {
        try {
            IngredientPrice updatedPrice = ingredientPriceService.updateIngredientPrice(id, updatedIngredientPrice);
            return ResponseEntity.ok(updatedPrice);
        } catch (IngredientPriceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IngredientPriceValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIngredientPrice(@PathVariable Long id) {
        try {
            ingredientPriceService.deleteIngredientPrice(id);
            return ResponseEntity.noContent().build();
        } catch (IngredientPriceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IngredientPriceInUseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientPrice> getIngredientPriceById(@PathVariable Long id) {
        return ingredientPriceService.getIngredientPriceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
