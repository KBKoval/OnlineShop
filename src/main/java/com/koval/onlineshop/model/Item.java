package com.koval.onlineshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@AllArgsConstructor
@Getter
@Setter
public class Item {
    private UUID id;
    private String description;
    private String idCloud;
    private int quantity;
    private float price;
}
