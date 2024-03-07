package org.danil.lab18.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientRepository {
    private final JdbcTemplate jdbcTemplate;

    public record Ingredient(Long id, String name) {

    }

    public List<Ingredient> getAll() {
        return jdbcTemplate.query("select id, name from ingredient",
                (rs, rowNum) -> new Ingredient(rs.getLong("id"), rs.getString("name"))
        );
    }

    public record IngredientQuantity(Long id, Short quantity) {

    }

    public void insertAll(long receiptId, List<IngredientQuantity> ingredients) {
        jdbcTemplate.batchUpdate("insert into receipt_ingredient(receipt_id, ingredient_id, quantity) values (?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final var ingredientQuantity = ingredients.get(i);
                ps.setLong(1, receiptId);
                ps.setLong(2, ingredientQuantity.id());
                ps.setShort(3, ingredientQuantity.quantity());
            }

            @Override
            public int getBatchSize() {
                return ingredients.size();
            }
        });
    }
}
