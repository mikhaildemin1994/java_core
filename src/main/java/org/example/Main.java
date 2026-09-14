package org.example;

public class Main {
    public static void main( String[] args ) {
        // Выполняем проверку метода с последующим выводом в консоль
        Converter converter = new Converter();
        Integer[] integers = {1,2,3,3,4,5,5,5,6,6,7};
        String[] strings = {"а", "а", "б", "в", "в", "в", "г", "д", "е", "f", "g", "g", "g"};
        System.out.println(converter.convertArrayToMap(integers));
    }
}
