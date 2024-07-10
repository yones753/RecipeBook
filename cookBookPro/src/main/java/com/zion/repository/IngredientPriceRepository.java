package com.zion.repository;

import com.zion.bean.IngredientPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientPriceRepository extends JpaRepository<IngredientPrice, Long> {
}