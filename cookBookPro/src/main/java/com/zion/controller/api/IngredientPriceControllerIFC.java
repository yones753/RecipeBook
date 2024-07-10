package com.zion.controller.api;

import com.zion.bean.IngredientPrice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface IngredientPriceControllerIFC {

    @Operation(summary = "Get all ingredient prices")
    @GetMapping
    ResponseEntity<?> getAllIngredientPrices();

    @Operation(summary = "Create a new ingredient price")
    @ApiResponse(responseCode = "200", description = "Successfully created ingredient price", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = IngredientPrice.class))})
    @PostMapping
    ResponseEntity<?> createIngredientPrice(@RequestBody IngredientPrice ingredientPrice);

    @Operation(summary = "Update an existing ingredient price by ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated ingredient price", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = IngredientPrice.class))})
    @ApiResponse(responseCode = "404", description = "Ingredient price not found")
    @PutMapping("/{id}")
    ResponseEntity<?> updateIngredientPrice(@PathVariable Long id, @RequestBody IngredientPrice updatedIngredientPrice);

    @Operation(summary = "Delete an ingredient price by ID")
    @ApiResponse(responseCode = "204", description = "Ingredient price deleted successfully")
    @ApiResponse(responseCode = "404", description = "Ingredient price not found")
    @ApiResponse(responseCode = "409", description = "Ingredient price in use by ingredients")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteIngredientPrice(@PathVariable Long id);

    @Operation(summary = "Get an ingredient price by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved ingredient price", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = IngredientPrice.class))})
    @ApiResponse(responseCode = "404", description = "Ingredient price not found")
    @GetMapping("/{id}")
    ResponseEntity<IngredientPrice> getIngredientPriceById(@PathVariable Long id);
}
