package org.danil.lab18.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class ReceiptRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertReceipt;

    public ReceiptRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertReceipt = new SimpleJdbcInsert(jdbcTemplate).withTableName("receipt").usingGeneratedKeyColumns("id");
    }


    public record ReceiptAndIngredients(Long id, String name, List<String> ingredients){

    }

    public List<ReceiptAndIngredients> getAll(String name) {
        return jdbcTemplate.query("""
                        select r.id, r.name, array_agg(i.name) as ingredient_names from receipt r
                            left join public.receipt_ingredient ri on r.id = ri.receipt_id
                            join public.ingredient i on ri.ingredient_id = i.id
                            where r.name ilike CONCAT('%', ?, '%')
                            group by r.id, r.name
                        """,
                new Object[]{name}, new int[]{Types.CHAR},
                (rs, rowNum) -> new ReceiptAndIngredients(
                        rs.getLong("id"),
                        rs.getString("name"),
                        Arrays.asList((String[]) rs.getArray("ingredient_names").getArray())
                )
        );
    }


    public Number insert(String name) {
        return insertReceipt.executeAndReturnKey(Map.of("name", name));
    }

    public int delete(long id) {
        return jdbcTemplate.update("delete from receipt where id = ?", id);
    }
}
