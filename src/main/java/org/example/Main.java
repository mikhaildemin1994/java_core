package org.example;

public class Main {
    public static void main(String[] args) {
        DataAggregator dataAggregator = new DataAggregator();
        ProductInfo info = dataAggregator.aggregateProductInfo("Ноутбук");
        System.out.println(info);
    }
}
