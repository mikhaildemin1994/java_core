package org.example;

public class Main {
    public static void main( String[] args ) {
        CustomBuilder customBuilder = new CustomBuilder();
        customBuilder.append("a");
        customBuilder.append("b");
        customBuilder.append("c");
        System.out.println(customBuilder);
        customBuilder.undo();
        System.out.println(customBuilder);
    }
}
