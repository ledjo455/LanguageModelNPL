package nlp;

import java.io.*;
import java.util.Scanner;

public class ModelFile extends Thread{

    private final File file; // txt to read
    private final MyHashTable model; // histogram / frequency table
    private final int nGram;
    private double norm;

    public ModelFile(File file, MyHashTable model, int nGram){
        super();
        this.nGram = nGram;
        this.model = model;
        this.file = file;
        norm = -1;
    }

    public ModelFile(File file, int nGram){
        super();
        this.nGram = nGram;
        this.model = new MyHashTable();
        this.file = file;
        norm = -1;
    }

    @Override
    public void run() {

        try (Scanner scanner = new Scanner(file)){

            //read the whole file
            scanner.useDelimiter("\\Z");
            if(!scanner.hasNext()) return; // if txt is empty
            String fileText = scanner.next();



            StringBuilder sb = new StringBuilder();

            fileText.chars().forEach(i -> { // n-gram
                        Character c = (char) i;
                        if(c.toString().matches("[A-Za-zÀ-ÖØ-öø-ÿ]")){ // if is letter
                            sb.append(c); // add letter
                            if(sb.length() >= nGram){
                                model.addNGram(sb.toString().toLowerCase());
                                sb.deleteCharAt(0);
                            }
                        }else{
                            sb.setLength(0); // clean
                        }
                    });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    private void calculateNorm(){

        int sum = model.values().stream()
                .mapToInt(i -> i * i)
                .sum();

        norm = Math.sqrt(sum);
    }

    synchronized public double getNorm(){
        if(norm == -1)
            calculateNorm();
        return norm;
    }

    public MyHashTable getVector(){
        return model;
    }

}