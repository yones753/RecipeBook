package com.zion.service;

import com.zion.bean.Ingredient;
import com.zion.bean.IngredientPrice;
import com.zion.repository.IngredientPriceRepository;
import com.zion.repository.IngredientRepository;
import com.zion.service.exceptions.IngredientPriceInUseException;
import com.zion.service.exceptions.IngredientPriceNotFoundException;
import com.zion.service.exceptions.IngredientPriceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientPriceService {

    @Autowired
    IngredientPriceRepository ingredientPriceRepository;

    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    RecipeService recipeService;


    public List<IngredientPrice> getAllIngredientPrices() {
        try {
            return ingredientPriceRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve ingredient prices", e);
        }
    }

    public Optional<IngredientPrice> getIngredientPriceById(Long id) {
        return ingredientPriceRepository.findById(id);
    }

    @Transactional
    public IngredientPrice updateIngredientPrice(Long id, IngredientPrice updatedIngredientPrice) {
        validateIngredientPrice(updatedIngredientPrice);
        return ingredientPriceRepository.findById(id).map(ingredientPrice -> {
            ingredientPrice.setPrice(updatedIngredientPrice.getPrice());
            IngredientPrice savedIngredientPrice = ingredientPriceRepository.save(ingredientPrice);
            recipeService.updateRecipesCostByIngredientPrice(savedIngredientPrice);
            return savedIngredientPrice;
        }).orElseThrow(() -> new IngredientPriceNotFoundException("Ingredient price with ID " + id + " not found"));
    }

    @Transactional
    public IngredientPrice addIngredientPrice(IngredientPrice ingredientPrice) {
        validateIngredientPrice(ingredientPrice);
        return ingredientPriceRepository.save(ingredientPrice);
    }

    private void validateIngredientPrice(IngredientPrice ingredientPrice) {
        if (ingredientPrice.getName() == null || ingredientPrice.getName().trim().isEmpty()) {
            throw new IngredientPriceValidationException("Name cannot be null or empty");
        }
        if (ingredientPrice.getPrice() < 1) {
            throw new IngredientPriceValidationException("Price must be greater than 1");
        }
    }

    @Transactional
    public void deleteIngredientPrice(Long id) {
        Optional<IngredientPrice> ingredientPriceOptional = ingredientPriceRepository.findById(id);
        if (ingredientPriceOptional.isPresent()) {
            List<Ingredient> ingredientsUsingPrice = ingredientRepository.findByIngredientPriceId(id);
            if (!ingredientsUsingPrice.isEmpty()) {
                throw new IngredientPriceInUseException("Cannot delete IngredientPrice because it is still in use by some ingredients.");
            }
            ingredientPriceRepository.deleteById(id);
        } else {
            throw new IngredientPriceNotFoundException("IngredientPrice with ID " + id + " not found");
        }
    }
}
