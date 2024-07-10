package com.zion.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zion.bean.Ingredient;
import com.zion.bean.IngredientPrice;
import com.zion.repository.IngredientPriceRepository;
import com.zion.repository.IngredientRepository;
import com.zion.service.exceptions.IngredientPriceInUseException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IngredientPriceServiceTest {

    @Mock
    private IngredientPriceRepository ingredientPriceRepository;
    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientPriceService ingredientPriceService;

    public IngredientPriceServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllIngredientPrices() {
        List<IngredientPrice> mockIngredientPrices = new ArrayList<>();
        mockIngredientPrices.add(new IngredientPrice(1L, "Ingredient A", 10.0));
        mockIngredientPrices.add(new IngredientPrice(2L, "Ingredient B", 15.0));

        when(ingredientPriceRepository.findAll()).thenReturn(mockIngredientPrices);

        List<IngredientPrice> result = ingredientPriceService.getAllIngredientPrices();

        assertEquals(2, result.size());
        assertEquals("Ingredient A", result.get(0).getName());
        assertEquals(10.0, result.get(0).getPrice());
        assertEquals("Ingredient B", result.get(1).getName());
        assertEquals(15.0, result.get(1).getPrice());

        verify(ingredientPriceRepository, times(1)).findAll();
    }

    @Test
    void testGetIngredientPriceById() {
        Long id = 1L;
        IngredientPrice mockIngredientPrice = new IngredientPrice(id, "Ingredient A", 10.0);

        when(ingredientPriceRepository.findById(id)).thenReturn(Optional.of(mockIngredientPrice));

        Optional<IngredientPrice> result = ingredientPriceService.getIngredientPriceById(id);

        assertTrue(result.isPresent());
        assertEquals("Ingredient A", result.get().getName());
        assertEquals(10.0, result.get().getPrice());

        verify(ingredientPriceRepository, times(1)).findById(id);
    }

    @Test
    void testAddIngredientPrice_ValidIngredientPrice_Success() {
        IngredientPrice newIngredientPrice = new IngredientPrice(null, "New Ingredient", 10.0);
        IngredientPrice savedIngredientPrice = new IngredientPrice(1L, "New Ingredient", 10.0);

        when(ingredientPriceRepository.save(any())).thenReturn(savedIngredientPrice);

        IngredientPrice result = ingredientPriceService.addIngredientPrice(newIngredientPrice);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Ingredient", result.getName());
        assertEquals(10.0, result.getPrice());

        verify(ingredientPriceRepository, times(1)).save(newIngredientPrice);
    }
    @Test
    void testDeleteIngredientPrice_ValidId_Success() {
        Long id = 1L;

        when(ingredientPriceRepository.findById(id)).thenReturn(Optional.of(new IngredientPrice(id, "Ingredient", 10.0)));
        doNothing().when(ingredientPriceRepository).deleteById(id);

        ingredientPriceService.deleteIngredientPrice(id);

        verify(ingredientPriceRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteIngredientPrice_IngredientPriceInUse_ExceptionThrown() {
        Long id = 1L;

        when(ingredientPriceRepository.findById(id)).thenReturn(Optional.of(new IngredientPrice(id, "Ingredient", 10.0)));
        when(ingredientRepository.findByIngredientPriceId(id)).thenReturn(List.of(new Ingredient()));

        assertThrows(IngredientPriceInUseException.class, () -> ingredientPriceService.deleteIngredientPrice(id));

        verify(ingredientPriceRepository, never()).deleteById(id);
    }

}
