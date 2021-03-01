package com.cft.demo;

public class Main {

    public static void main(String[] args) {

        try{
            DemoMergeSort mergeSort = DemoConsoleArgsParser.getSortFromParseArgs(args);
            mergeSort.sort();
        }catch (RuntimeException exception){
            System.out.println("Something go wrong:");
            System.out.println(exception.getMessage());
            System.out.println("Application was closed");
        }

    }
}
