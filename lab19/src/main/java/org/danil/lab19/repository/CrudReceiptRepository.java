package org.danil.lab19.repository;

import org.danil.lab19.model.Receipt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CrudReceiptRepository extends CrudRepository<Receipt, Long> {
    List<Receipt> findAllByNameContaining(String name);
}