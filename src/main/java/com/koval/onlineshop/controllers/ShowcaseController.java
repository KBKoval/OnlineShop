package com.koval.onlineshop.controllers;

import com.koval.onlineshop.model.Item;
import com.koval.onlineshop.services.interfaces.WorkingWithShowcase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/*
посмотреть каталог getListCatalog()
посмотреть описание товара view the product description
положить в  корзину put the goods in the basket
 */
@RestController
@Validated
@CrossOrigin("*")
@RequestMapping("api/katalog")
public class ShowcaseController {
    private final WorkingWithShowcase workingWithShowcase;

    @Autowired
    public ShowcaseController(WorkingWithShowcase workingWithShowcase) {
        this.workingWithShowcase = workingWithShowcase;
    }

    /**
     * Возвращает список товаров
     *
     * @return список товаров
     */
    @GetMapping(value = "all")
    public HashMap<UUID, Item> getListCatalog() {
        return workingWithShowcase.getListCatalog();
    }

    /**
     * аолучить  описание товара
     *
     * @param id идентификатор товара
     * @return описание
     */
    @GetMapping(value = "{id}")
    public String getDescription(@Valid @PathVariable("id") String id) {
        return workingWithShowcase.getDescription(id);
    }
}
