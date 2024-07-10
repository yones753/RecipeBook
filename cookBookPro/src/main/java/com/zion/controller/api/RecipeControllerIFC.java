package com.zion.controller.api;

import com.zion.bean.Recipe;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RecipeControllerIFC {

    @Operation(summary = "Get all recipes")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))})
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    ResponseEntity<?> getAllRecipes();

    @Operation(summary = "Get a recipe by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recipe", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))})
    @ApiResponse(responseCode = "404", description = "Recipe not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}")
    ResponseEntity<Recipe> getRecipeById(@PathVariable Long id);

    @Operation(summary = "Create a new recipe")
    @ApiResponse(responseCode = "200", description = "Successfully created recipe", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))})
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    ResponseEntity<?> createRecipe(@RequestBody Recipe recipe);

    @Operation(summary = "Update an existing recipe by ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated recipe", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))})
    @ApiResponse(responseCode = "404", description = "Recipe not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PutMapping("/{id}")
    ResponseEntity<?> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipeDetails);

    @Operation(summary = "Delete a recipe by ID")
    @ApiResponse(responseCode = "204", description = "Recipe deleted successfully")
    @ApiResponse(responseCode = "404", description = "Recipe not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteRecipe(@PathVariable Long id);
}
