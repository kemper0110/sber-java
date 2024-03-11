package org.danil.lab19.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class ReceiptIngredientId implements Serializable {
    private static final long serialVersionUID = -7689437125399189437L;
    @Column(name = "receipt_id", nullable = false)
    private Long receiptId;

    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReceiptIngredientId entity = (ReceiptIngredientId) o;
        return Objects.equals(this.ingredientId, entity.ingredientId) &&
               Objects.equals(this.receiptId, entity.receiptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, receiptId);
    }

}