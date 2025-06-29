package com.koval.onlineshop.model;

import java.util.UUID;

public record Item (UUID id,String description,String idCloud, int quantity, float price){}
