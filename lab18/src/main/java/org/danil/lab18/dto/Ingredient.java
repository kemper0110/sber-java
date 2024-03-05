package org.danil.lab18.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Ingredient {
    private Long id;
    private String name;
}
