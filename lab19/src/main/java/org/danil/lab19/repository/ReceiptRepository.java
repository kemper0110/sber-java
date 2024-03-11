package org.danil.lab19.repository;

import org.danil.lab19.dto.ReceiptInfo;
import org.danil.lab19.model.Receipt;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    @EntityGraph(attributePaths = {"receiptIngredient", "receiptIngredient.ingredient"})
    List<ReceiptInfo> findAllByNameContaining(String name);
}