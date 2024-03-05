package org.danil.lab18;

import org.danil.lab18.dto.Ingredient;
import org.danil.lab18.dto.Receipt;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert insertReceipt;

    public ReceiptController(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedParameterJdbcTemplate;
        this.insertReceipt = new SimpleJdbcInsert(jdbcTemplate).withTableName("receipt").usingGeneratedKeyColumns("id");
    }

    @GetMapping
    List<Receipt> index(@RequestParam(name = "name", required = false, defaultValue = "") String nameQuery) {
        return jdbcTemplate.query("select id, name from receipt where name ilike CONCAT('%', ?, '%')",
                new Object[]{nameQuery}, new int[]{Types.CHAR},
                (rs, rowNum) -> new Receipt(rs.getLong("id"), rs.getString("name"))
        );
    }


    @GetMapping("/create")
    Map<String, Object> create() {
        return Map.of(
                "ingredients",
                jdbcTemplate.query("select id, name from ingredient",
                        (rs, rowNum) -> new Ingredient(rs.getLong("id"), rs.getString("name"))
                )
        );
    }


    record IngredientQuantity(Long id, Short quantity) {

    }

    record CreateReceiptRequest(String name, List<IngredientQuantity> ingredients) {
    }

    @PostMapping
    @Transactional
    void store(@RequestBody CreateReceiptRequest createReceiptRequest) {
        final var receiptId = insertReceipt.executeAndReturnKey(Map.of("name", createReceiptRequest.name())).intValue();
        jdbcTemplate.batchUpdate("insert into receipt_ingredient(receipt_id, ingredient_id, quantity) values (?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final var ingredientQuantity = createReceiptRequest.ingredients().get(i);
                ps.setLong(1, receiptId);
                ps.setLong(2, ingredientQuantity.id());
                ps.setShort(3, ingredientQuantity.quantity());
            }

            @Override
            public int getBatchSize() {
                return createReceiptRequest.ingredients().size();
            }
        });
    }



    @DeleteMapping("/{id}")
    int delete(@PathVariable Long id) {
        return jdbcTemplate.update("delete from receipt where id = ?", id);
    }
}
