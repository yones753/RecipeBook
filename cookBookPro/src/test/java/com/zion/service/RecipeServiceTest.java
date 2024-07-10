package com.zion.service;

import com.zion.bean.Ingredient;
import com.zion.bean.IngredientPrice;
import com.zion.bean.Recipe;
import com.zion.repository.IngredientPriceRepository;
import com.zion.repository.RecipeRepository;
import com.zion.service.exceptions.RecipeFieldsEmptyException;
import com.zion.service.exceptions.RecipeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientPriceRepository ingredientPriceRepository;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRecipes() {
        List<Recipe> mockRecipes = new ArrayList<>();
        when(recipeRepository.findAll()).thenReturn(mockRecipes);

        List<Recipe> recipes = recipeService.getAllRecipes();

        assertNotNull(recipes);
        assertEquals(mockRecipes, recipes);
    }

    @Test
    void testGetRecipeById_ValidId_ReturnRecipe() {
        Long recipeId = 1L;
        Recipe mockRecipe = new Recipe();
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

        Recipe recipe = recipeService.getRecipeById(recipeId);

        assertNotNull(recipe);
        assertEquals(mockRecipe, recipe);
    }

    @Test
    void testGetRecipeById_InvalidId_ReturnNull() {
        Long invalidId = 99L;
        when(recipeRepository.findById(invalidId)).thenReturn(Optional.empty());

        Recipe recipe = recipeService.getRecipeById(invalidId);

        assertNull(recipe);
    }

    @Test
    void testCreateRecipe_ValidRecipe_Success() {
        Recipe mockRecipe = new Recipe();
        mockRecipe.setRecipeName("Test Recipe");
        mockRecipe.setRecipeInstructions("Test Instructions");

        IngredientPrice mockIngredientPrice = new IngredientPrice();
        mockIngredientPrice.setId(2L);
        mockIngredientPrice.setName("Tomato");
        mockIngredientPrice.setPrice(1.99);

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantity(453);
        ingredient.setName("Tomato");
        ingredient.setIngredientPriceId(2L);

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);

        mockRecipe.setRecipeIngredients(ingredients);

        when(ingredientPriceRepository.findById(2L)).thenReturn(Optional.of(mockIngredientPrice));
        when(recipeRepository.save(any())).thenReturn(mockRecipe);

        Recipe createdRecipe = recipeService.createRecipe(mockRecipe);

        assertNotNull(createdRecipe);
        assertEquals("Test Recipe", createdRecipe.getRecipeName());
        assertEquals("Test Instructions", createdRecipe.getRecipeInstructions());

        assertNotNull(createdRecipe.getRecipeIngredients());
        assertEquals(1, createdRecipe.getRecipeIngredients().size());
        Ingredient createdIngredient = createdRecipe.getRecipeIngredients().get(0);
        assertEquals(453, createdIngredient.getQuantity());
        assertEquals("Tomato", createdIngredient.getName());
        assertEquals(2L, createdIngredient.getIngredientPriceId());
    }



    @Test
    void testCreateRecipe_NullName_ExceptionThrown() {
        Recipe recipe = new Recipe();
        recipe.setRecipeInstructions("Test Instructions");
        recipe.setRecipeIngredients(new ArrayList<>());

        assertThrows(RecipeFieldsEmptyException.class, () -> {
            recipeService.createRecipe(recipe);
        });
    }

    @Test
    void testUpdateRecipe_ValidIdAndRecipe_Success() {
        Long recipeId = 1L;

        Recipe existingRecipe = new Recipe();
        existingRecipe.setRecipeId(recipeId);
        existingRecipe.setRecipeName("Existing Recipe");
        existingRecipe.setRecipeInstructions("Existing Instructions");

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setRecipeName("Updated Recipe");
        updatedRecipe.setRecipeInstructions("Updated Instructions");

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(existingRecipe));
        when(recipeRepository.save(any())).thenReturn(updatedRecipe);

        Recipe result = recipeService.updateRecipe(recipeId, updatedRecipe);

        assertNotNull(result);
        assertEquals("Updated Recipe", result.getRecipeName());
        assertEquals("Updated Instructions", result.getRecipeInstructions());
    }


    @Test
    void testUpdateRecipe_InvalidId_ExceptionThrown() {
        Long invalidId = 99L;
        Recipe updatedRecipe = new Recipe();

        when(recipeRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.updateRecipe(invalidId, updatedRecipe);
        });
    }

    @Test
    void testDeleteRecipe_ValidId_Success() {
        Long recipeId = 1L;
        Recipe mockRecipe = new Recipe();
        mockRecipe.setRecipeId(recipeId);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

        recipeService.deleteRecipe(recipeId);

        verify(recipeRepository, times(1)).deleteById(recipeId);
    }

    @Test
    void testDeleteRecipe_InvalidId_ExceptionThrown() {
        Long invalidId = 99L;

        when(recipeRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.deleteRecipe(invalidId);
        });
    }

}
