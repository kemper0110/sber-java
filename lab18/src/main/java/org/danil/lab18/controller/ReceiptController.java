package org.danil.lab18.controller;

import lombok.RequiredArgsConstructor;
import org.danil.lab18.repository.IngredientRepository;
import org.danil.lab18.service.ReceiptService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptService receiptService;

    @GetMapping
    Map<String, ?> index(@RequestParam(name = "name", required = false, defaultValue = "") String nameQuery) {
        return receiptService.index(nameQuery);
    }

    @GetMapping("/create")
    Map<String, ?> create() {
        return receiptService.create();
    }


    public record CreateReceiptRequest(String name, List<IngredientRepository.IngredientQuantity> ingredients) {
    }

    @PostMapping
    void store(@RequestBody CreateReceiptRequest createReceiptRequest) {
        receiptService.store(createReceiptRequest.name(), createReceiptRequest.ingredients());
    }

    @DeleteMapping("/{id}")
    int delete(@PathVariable long id) {
        return receiptService.delete(id);
    }
}
