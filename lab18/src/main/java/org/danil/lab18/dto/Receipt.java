package org.danil.lab18.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Receipt {
    private Long id;
    private String name;
//    private List<ReceiptIngredient> ingredients;
}
