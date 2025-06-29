package com.koval.onlineshop.controllers;

import com.koval.onlineshop.model.Item;

import jakarta.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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


    /**
     * Возвращает список товаров
     *
     * @return список товаров
     */
    @GetMapping(value = "all")
    public HashMap<UUID, Item> getListCatalog() {
        return  null;
    }

    /**
     * Получить  описание товара
     *
     * @param id идентификатор товара
     * @return описание
     */
    @GetMapping(value = "{id}")
    public String getDescription(@Valid @PathVariable("id") String id) {
        return null;
    }
}
