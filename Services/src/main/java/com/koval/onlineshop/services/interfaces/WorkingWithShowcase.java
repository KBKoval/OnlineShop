package com.koval.onlineshop.services.interfaces;

import com.koval.onlineshop.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface WorkingWithShowcase {
    public HashMap<UUID, Item> getListCatalog();
    public String getDescription(String id);
}
