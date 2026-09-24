package org.example;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class DataAggregator {
    private final int from = 1000;
    private final int to= 3000;

    private double fetchPrice() {
        if(ThreadLocalRandom.current().nextInt(5) != 0) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(from, to));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else {
            throw new RuntimeException();
        }
        return Math.round(new Random().nextDouble() * 1000.0);
    }

    private String fetchDescription() {
        if(ThreadLocalRandom.current().nextInt(5) != 0) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(from, to));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else {
            throw new RuntimeException();
        }
        return "Какое-нибудь описание";
    }

    private double fetchRating() {
        if(ThreadLocalRandom.current().nextInt(5) != 0) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(from, to));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else {
            throw new RuntimeException();
        }
        return Math.round(new Random().nextDouble() * 5.0);
    }

    public ProductInfo aggregateProductInfo(String productName) {
        CompletableFuture<Double> priceOfProduct = CompletableFuture
                .supplyAsync(this::fetchPrice)
                .exceptionally(ex -> 0.00);

        CompletableFuture<String> descriptionOfProduct = CompletableFuture
                .supplyAsync(this::fetchDescription)
                .exceptionally(ex -> "Нет данных");

        CompletableFuture<Double> ratingOfProduct = CompletableFuture
                .supplyAsync(this::fetchRating)
                .exceptionally(ex -> 0.00);

        CompletableFuture<Void> allOffFuture = CompletableFuture
                .allOf(priceOfProduct, descriptionOfProduct, ratingOfProduct);
        allOffFuture.join();

        double price = priceOfProduct.join();
        String description = descriptionOfProduct.join();
        double rating = ratingOfProduct.join();

        return new ProductInfo(productName, price, description, rating);
    }
}
