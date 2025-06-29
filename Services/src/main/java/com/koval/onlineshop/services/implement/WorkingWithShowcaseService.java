package com.koval.onlineshop.services.implement;

import com.koval.onlineshop.model.Item;
import com.koval.onlineshop.repositories.interfaces.ShowCase;
import com.koval.onlineshop.services.interfaces.WorkingWithShowcase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class WorkingWithShowcaseService implements WorkingWithShowcase {
    private final ShowCase showCase;

    private final HashMap<UUID, Item> items =new HashMap<>();

    @Autowired
    public WorkingWithShowcaseService(ShowCase showCase) {
        this.showCase = showCase;
    }

    @Cacheable(value = "listItems", cacheManager = "items")
    public HashMap<UUID, Item> getListCatalog() {
        showCase.getItems().parallelStream().forEach(element->this.items.put(element.id(),element));
        return this.items;
    }

    @Cacheable(value = "listItems", cacheManager = "items", key = "id")
    public String getDescription(String id){
        return this.items.get(UUID.fromString(id)).description();
    }
}
