package com.cft.demo;

import java.util.*;

public class DemoConsoleArgsParser {

    private static String sortDirection;
    private static String sortDataType;
    private static String outputFileDir;
    private static List<String> inputFileDir = new ArrayList<>();

    private static List<String> keys = new ArrayList<>();
    private static List<String> values = new ArrayList<>();

    public static DemoMergeSort getSortFromParseArgs(String[] args){
        parseKeysAndValues(args);
        if(keys.size()==0 || values.size()==0) throw new RuntimeException("Required parameters are not specified");

        setSortDirectionFromKeys();
        setSortDataType();
        setOutputFileDir();
        setInputFileDir();

        return new DemoMergeSort(sortDirection, sortDataType, outputFileDir, inputFileDir);
    }

    private static void parseKeysAndValues(String[] args){
        for (String item: args) {
            if(item.charAt(0)=='-') keys.add(item);
            else values.add(item);
        }
    }

    private static void setSortDirectionFromKeys(){
        for (String item: keys) {
            if(item.equals("-a")){
                sortDirection = "asc";
                return;
            }
            if(item.equals("-d")){
                sortDirection = "desc";
                return;
            }
        }
        sortDirection = "asc";
    }

    private static void setSortDataType(){
        for (String item: keys) {
            if(item.equals("-s")){
                sortDataType = "string";
                return;
            }
            if(item.equals("-i")){
                sortDataType = "integer";
                return;
            }
        }
        throw new RuntimeException("Required parameters are not specified");
    }

    private static void setOutputFileDir(){
        outputFileDir = values.get(0);
        values.remove(0);
    }

    private static void setInputFileDir(){
        inputFileDir.addAll(0, values);
    }

}
