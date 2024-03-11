package org.danil.lab19.dto;

import java.util.Collection;

/**
 * Projection for {@link org.danil.lab19.model.Receipt}
 */
public interface ReceiptInfo {
    Long getId();

    String getName();

    Collection<ReceiptIngredientInfo> getReceiptIngredient();

    /**
     * Projection for {@link org.danil.lab19.model.ReceiptIngredient}
     */
    interface ReceiptIngredientInfo {
        Short getQuantity();

        IngredientInfo getIngredient();

        /**
         * Projection for {@link org.danil.lab19.model.Ingredient}
         */
        interface IngredientInfo {
            Long getId();

            String getName();
        }
    }
}