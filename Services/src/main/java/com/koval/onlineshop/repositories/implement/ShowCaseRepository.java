package com.koval.onlineshop.repositories.implement;

import com.koval.onlineshop.mappers.ItemMapper;
import com.koval.onlineshop.model.Item;
import com.koval.onlineshop.repositories.interfaces.ShowCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShowCaseRepository implements ShowCase {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ShowCaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Item> getItems() {
        String sqlQuery = "select id as text, description, id_cloud, quantity, price from items where flag_showcase=true";
        return this.jdbcTemplate.query(sqlQuery, new ItemMapper() );
    }
}
