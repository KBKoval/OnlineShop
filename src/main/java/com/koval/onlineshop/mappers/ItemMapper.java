package com.koval.onlineshop.mappers;

import com.koval.onlineshop.model.Item;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ItemMapper implements RowMapper<Item> {
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException{
        String idCloud = "https://mega.nz/file/"+rs.getString(rs.getString(2) );
        return new Item(UUID.fromString(rs.getString(1)),rs.getString(2),idCloud,rs.getInt(3),rs.getFloat(4));
    }
}
