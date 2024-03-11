package org.danil.lab19.service;

import lombok.RequiredArgsConstructor;
import org.danil.lab19.repository.CrudReceiptRepository;
import org.danil.lab19.repository.IngredientRepository;
import org.danil.lab19.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final IngredientRepository ingredientRepository;
    private final CrudReceiptRepository crudReceiptRepository;

    public Map<String, ?> index(String nameQuery) {
        return Map.of(
                "receipts", crudReceiptRepository.findAllByNameContaining(nameQuery)
        );
    }

    public Map<String, ?> create() {
        return Map.of(
                "ingredients", ingredientRepository.getAll()
        );
    }

    @Transactional
    public void store(String receiptName, List<IngredientRepository.IngredientQuantity> ingredients) {
        final var receiptId = receiptRepository.insert(receiptName);
        ingredientRepository.insertAll(receiptId.longValue(), ingredients);
    }

    public int delete(long id) {
        return receiptRepository.delete(id);
    }
}
