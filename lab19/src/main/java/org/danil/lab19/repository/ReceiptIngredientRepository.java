package org.danil.lab19.repository;

import org.danil.lab19.model.ReceiptIngredient;
import org.danil.lab19.model.ReceiptIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptIngredientRepository extends JpaRepository<ReceiptIngredient, ReceiptIngredientId> {
}