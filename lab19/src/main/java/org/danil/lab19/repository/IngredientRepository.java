package org.danil.lab19.repository;

import org.danil.lab19.dto.IngredientInfo;
import org.danil.lab19.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Query
    List<IngredientInfo> findAllProjectedBy();
}