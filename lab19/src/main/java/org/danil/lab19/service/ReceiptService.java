package org.danil.lab19.service;

import lombok.RequiredArgsConstructor;
import org.danil.lab19.dto.IngredientQuantity;
import org.danil.lab19.model.Receipt;
import org.danil.lab19.model.ReceiptIngredient;
import org.danil.lab19.model.ReceiptIngredientId;
import org.danil.lab19.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final IngredientRepository ingredientRepository;
    private final ReceiptIngredientRepository receiptIngredientRepository;

    public Map<String, ?> index(String nameQuery) {
        return Map.of(
                "receipts", receiptRepository.findAllByNameContaining(nameQuery)
        );
    }

    public Map<String, ?> create() {
        return Map.of(
                "ingredients", ingredientRepository.findAllProjectedBy()
        );
    }

    @Transactional
    public void store(String receiptName, List<IngredientQuantity> ingredients) {
        final var receipt = new Receipt(null, receiptName, null);

        final var receiptIngredients = ingredients.stream()
                .map(ing -> new ReceiptIngredient(
                        new ReceiptIngredientId(receipt.getId(), ing.id()),
                        receipt,
                        ingredientRepository.getReferenceById(ing.id()),
                        ing.quantity()
                ))
                .toList();
        receipt.setReceiptIngredient(receiptIngredients);

        receiptRepository.save(receipt);
        receiptIngredientRepository.saveAll(receiptIngredients);
    }

    public void delete(long id) {
        receiptRepository.deleteById(id);
    }
}
