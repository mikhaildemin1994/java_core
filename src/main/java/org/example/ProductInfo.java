package org.example;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ProductInfo {
    private String name;
    private double price;
    private String description;
    private double rating;
}
