package com.cft.demo;

import java.io.*;
import java.util.*;

public class DemoMergeSort {

    private final String sortDirection;
    private final String sortDataType;
    private final String outputFileName;
    private final List<String> inputFileName;

    private List<BufferedReader> input;
    private int countOfActiveInputFiles;
    private List<String> valuesForCompare;
    private BufferedWriter output;
    private String lastRecordedValue;

    public DemoMergeSort(String sortDirection, String sortDataType, String outputFileName, List<String> inputFileName) {
        this.sortDirection = sortDirection;
        this.sortDataType = sortDataType;
        this.outputFileName = outputFileName;
        this.inputFileName = inputFileName;
    }

    public void sort(){
        init();
        int index;

        while (countOfActiveInputFiles >0) {

            if(sortDirection.equals("asc")) {
                index = getIndexOfMinValue();
            }else {
                index = getIndexOfMaxValue();
            }

            String currentValue = valuesForCompare.get(index);

            if(!chekValue(currentValue)){
                System.out.println("Value: " + currentValue + " incorrect. Missing.");
                loadNextValue(index);
                continue;
            }

            lastRecordedValue = currentValue;
            writeToFile(currentValue);
            loadNextValue(index);
        }

        try {
            output.close();
        }catch (IOException exception){
            System.out.println(exception.getMessage());
        }

        System.out.println("Sort is successfully ending");
        deleteTempFiles();
    }

    private boolean chekValue(String value){
        if(lastRecordedValue == null) return true;

        if(sortDirection.equals("asc") && (compare(value, lastRecordedValue)>=0)) {
            return true;
        }
        if(sortDirection.equals("desc") && (compare(value, lastRecordedValue)<=0)) {
            return true;
        }

        return false;
    }

    private void loadNextValue(int index){
        try{
            String value = input.get(index).readLine();
            valuesForCompare.set(index, value);
            if(value == null){
                input.get(index).close();
                input.set(index, null);

                valuesForCompare.removeIf(Objects::isNull);
                input.removeIf(Objects::isNull);
                countOfActiveInputFiles--;
            }

        }catch (IOException exception){
            System.out.println(exception.getMessage());
        }
    }

    private int getIndexOfMaxValue(){
        int indexOfMaxValue = 0;

        for (int i = 1; i < valuesForCompare.size(); i++) {
            String max = valuesForCompare.get(indexOfMaxValue);
            String current = valuesForCompare.get(i);
            if(compare(max, current)<0){
                indexOfMaxValue = i;
            }
        }

        return indexOfMaxValue;
    }

    private int getIndexOfMinValue(){
        int indexOfMinValue = 0;

        for (int i = 1; i < valuesForCompare.size(); i++) {
            String min = valuesForCompare.get(indexOfMinValue);
            String current = valuesForCompare.get(i);
            if(compare(min, current)>0){
                indexOfMinValue = i;
            }
        }

        return indexOfMinValue;
    }

    private int compare(String s1, String s2){
        if(sortDataType.equals("string")){
            return s1.compareTo(s2);
        }else{
            int i1, i2;
            try{
                i1 = Integer.parseInt(s1);
                i2 = Integer.parseInt(s2);
                return i1-i2;
            }catch (NumberFormatException exception){
                throw new RuntimeException(exception.getMessage());
            }
        }
    }

    private void init(){

        for(int i=0; i<inputFileName.size(); i++){
            String fileName = inputFileName.get(i);
            boolean isSorted = fileCheck(inputFileName.get(i));
            System.out.print(fileName + " is sorted: " + isSorted);
            if(!isSorted){
                System.out.print("\t Creating temp file");
                inputFileName.set(i, getReversFile(fileName));
            }
            System.out.println();
        }

        input = openInputFiles();
        countOfActiveInputFiles = input.size();
        if(countOfActiveInputFiles == 0) throw new RuntimeException("No files were opened");

        valuesForCompare = new ArrayList<>(countOfActiveInputFiles);
        output = openOutputFile();

        for(int i = 0; i< countOfActiveInputFiles; i++){
            valuesForCompare.add(i, null);
        }

        loadValuesFromFileInput();
    }

    private BufferedWriter openOutputFile(){
        File file = new File(outputFileName);
        if(!file.exists()){
            try {
                if(!file.createNewFile()){
                    throw new RuntimeException("Can't create new file");
                }
            }catch (IOException exception){
                System.out.println(exception.getMessage());
            }
        }

        try {
            return new BufferedWriter(new FileWriter(file));
        }catch (IOException exception){
            throw new RuntimeException(exception.getMessage());
        }

    }

    private List<BufferedReader> openInputFiles(){
        if(inputFileName.isEmpty()) throw new RuntimeException("no file to open");

        List<BufferedReader> result = new ArrayList<>();

        for (String input: inputFileName) {
            File file = new File(input);

            try {
                result.add(new BufferedReader(new FileReader(file)));
            }catch (FileNotFoundException exception){
                System.out.println("File: " + input + " not found");
            }
        }

        return result;
    }

    private void loadValuesFromFileInput(){
        try{
            int countOfNullValues = 0;
            for(int i = 0; i< countOfActiveInputFiles; i++){
                String nextLine  = input.get(i).readLine();
                valuesForCompare.set(i, nextLine);
                if(nextLine == null){
                    countOfNullValues++;
                    input.get(i).close();
                    input.set(i, null);
                }
            }

            input.removeIf(Objects::isNull);
            valuesForCompare.removeIf(Objects::isNull);
            countOfActiveInputFiles -=countOfNullValues;
        }catch (IOException exception){
            System.out.println(exception.getMessage());
        }
    }

    private void writeToFile(String value){
        try{
            output.write(value);
            output.newLine();
        }catch (IOException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

    private String getReversFile(String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            throw new RuntimeException("File: " + fileName + " not found");
        }
        String tempFileName = fileName.replace(".txt", "_temp.txt");
        File tempFile = new File(tempFileName);
        BufferedReader reader;
        BufferedWriter writer;
        int bufferSize = 100;
        List<String> buffer = new ArrayList<>(bufferSize);
        try {
            reader = new BufferedReader(new FileReader(file));
            if(!tempFile.createNewFile()){
                throw new RuntimeException("Can't create new file");
            }
            writer = new BufferedWriter(new FileWriter(tempFile));

            boolean isEOF = false;
            long valuesCount = 0L;
            long valuesRecorded = 0L;

            while(true){
                buffer.clear();
                for (int i = 0; i < bufferSize; i++) {
                    String nextValue = reader.readLine();
                    if(nextValue == null){
                        isEOF = true;
                        break;
                    }
                    buffer.add(i, nextValue);
                    valuesCount++;
                }
                buffer.removeIf(Objects::isNull);
                if(isEOF){
                    for(int i=buffer.size()-1; i>=0; i--){
                        writer.write(buffer.get(i));
                        writer.newLine();
                        valuesRecorded++;
                    }
                    break;
                }
            }

            long iterationCount = (valuesCount - valuesRecorded) / bufferSize;

            for (long i = 0; i<iterationCount; i++){
                reader.close();
                reader = new BufferedReader(new FileReader(file));

                for(long j = 0; j<iterationCount-i; j++){
                    buffer.clear();
                    for(int k = 0; k<bufferSize; k++){
                        String temp = reader.readLine();
                        if(j+1 == iterationCount-i){
                            buffer.add(k, temp);
                        }
                    }
                }
                for(int l=bufferSize-1; l>=0; l--){
                    writer.write(buffer.get(l));
                    writer.newLine();
                }
            }
            reader.close();
            writer.close();
        } catch (IOException exception){
            throw new RuntimeException(exception.getMessage());
        }

        return tempFileName;
    }

    private boolean fileCheck(String inputFileName){
        final int COUNT_VALUES_FOR_TEST = 10;
        File file = new File(inputFileName);
        List<String> test = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(true){
                String temp = reader.readLine();
                if(temp == null) break;
                test.add(temp);
                if(test.size()>COUNT_VALUES_FOR_TEST) break;
            }
            reader.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }

        int asc=0, desc=0;
        for(int i=0; i<test.size()-1; i++){
            String s1 = test.get(i), s2 = test.get(i+1);
            int compareResult = compare(s1,s2);
            if(compareResult>0) desc++;
            if(compareResult<0) asc++;
        }

        if(sortDirection.equals("asc") && (asc<desc)) return false;
        if(sortDirection.equals("desc") && (asc>desc)) return false;

        return true;
    }

    private void deleteTempFiles(){
        for (String fileName: inputFileName) {

            if(fileName.endsWith("_temp.txt")){
                System.out.println("Deleting temp file: " + fileName);
                File file = new File(fileName);
                file.delete();
            }
        }
    }

    @Override
    public String toString() {
        return "DemoMergeSort{" +
                "sortDirection='" + sortDirection + '\'' +
                ", sortDataType='" + sortDataType + '\'' +
                ", outputFileDir='" + outputFileName + '\'' +
                ", inputFileDir=" + inputFileName +
                '}';
    }
}
