package com.zion.service;

import com.zion.bean.Ingredient;
import com.zion.bean.IngredientPrice;
import com.zion.bean.Recipe;
import com.zion.repository.IngredientPriceRepository;
import com.zion.repository.RecipeRepository;
import com.zion.service.exceptions.RecipeFieldsEmptyException;
import com.zion.service.exceptions.RecipeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    IngredientPriceRepository ingredientPriceRepository;


    public List<Recipe> getAllRecipes() {
        try {
            return recipeRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve recipes", e);
        }
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    @Transactional
    public Recipe createRecipe(Recipe recipe) {
        if (recipe.getRecipeName() == null || recipe.getRecipeName().trim().isEmpty()) {
            throw new RecipeFieldsEmptyException("Recipe name cannot be blank");
        }
        if (recipe.getRecipeInstructions() == null || recipe.getRecipeInstructions().trim().isEmpty()) {
            throw new RecipeFieldsEmptyException("Recipe instructions cannot be blank");
        }
        if (recipe.getRecipeIngredients() == null || recipe.getRecipeIngredients().isEmpty()) {
            throw new RecipeFieldsEmptyException("Recipe must have ingredients");
        }
        recipe.setRecipeCost(calculateRecipeCost(recipe));
        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        try {
            Recipe existingRecipe = recipeRepository.findById(id)
                    .orElseThrow(() -> new RecipeNotFoundException("Invalid recipe ID"));

            existingRecipe.setRecipeName(updatedRecipe.getRecipeName());
            existingRecipe.setRecipeInstructions(updatedRecipe.getRecipeInstructions());

            if (!compareIngredients(existingRecipe.getRecipeIngredients(), updatedRecipe.getRecipeIngredients())) {
                double totalCost = 0.0;
                for (Ingredient ingredient : updatedRecipe.getRecipeIngredients()) {
                    IngredientPrice ingredientPrice = ingredientPriceRepository.findById(ingredient.getIngredientPriceId())
                            .orElseThrow(() -> new RecipeNotFoundException("Invalid ingredient price ID"));
                    totalCost += ingredient.getQuantity() * ingredientPrice.getPrice();
                }
                existingRecipe.setRecipeCost(totalCost);
                existingRecipe.getRecipeIngredients().clear();
                existingRecipe.getRecipeIngredients().addAll(updatedRecipe.getRecipeIngredients());
            }
            return recipeRepository.save(existingRecipe);
        } catch (RuntimeException e) {
            throw new RecipeNotFoundException("Error updating recipe");
        }
    }

    public void deleteRecipe(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            recipeRepository.deleteById(id);
        } else {
            throw new RecipeNotFoundException("Recipe with id " + id + " not found");
        }
    }

    private boolean compareIngredients(List<Ingredient> list1, List<Ingredient> list2) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }
        return list1.containsAll(list2) && list2.containsAll(list1);
    }

    @Transactional
    public void updateRecipesCostByIngredientPrice(IngredientPrice updatedIngredientPrice) {
        List<Recipe> recipes = recipeRepository.findAll();
        for (Recipe recipe : recipes) {
            boolean containsUpdatedIngredient = recipe.getRecipeIngredients().stream()
                    .anyMatch(ingredient -> ingredient.getIngredientPriceId().equals(updatedIngredientPrice.getId()));
            if (containsUpdatedIngredient) {
                double totalCost = calculateRecipeCostAfterChangePrice(recipe, updatedIngredientPrice);
                recipe.setRecipeCost(totalCost);
                recipeRepository.save(recipe);
            }
        }
    }

    private double calculateRecipeCostAfterChangePrice(Recipe recipe, IngredientPrice updatedIngredientPrice) {
        double totalCost = 0.0;
        for (Ingredient ingredient : recipe.getRecipeIngredients()) {
            if (ingredient.getIngredientPriceId().equals(updatedIngredientPrice.getId())) {
                totalCost += ingredient.getQuantity() * updatedIngredientPrice.getPrice();
            } else {
                IngredientPrice originalPrice = ingredientPriceRepository.findById(ingredient.getIngredientPriceId()).orElse(null);
                if (originalPrice != null) {
                    double price = originalPrice.getPrice() == 0 ? 1 : originalPrice.getPrice();
                    totalCost += ingredient.getQuantity() * price;
                }
            }
        }
        return totalCost;
    }

    private double calculateRecipeCost(Recipe recipe) {
        double totalCost = 0.0;
        for (Ingredient ingredient : recipe.getRecipeIngredients()) {
            IngredientPrice ingredientPrice = ingredientPriceRepository.findById(ingredient.getIngredientPriceId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ingredient price ID"));
            totalCost += ingredient.getQuantity() * ingredientPrice.getPrice();
        }
        return totalCost;
    }

}
