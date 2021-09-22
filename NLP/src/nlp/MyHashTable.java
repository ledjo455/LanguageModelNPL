package nlp;


import java.util.Hashtable;

public class MyHashTable extends Hashtable<String, Integer> {

    synchronized public void addNGram(String n){
        Integer previous;
        if( (previous = put(n, 1)) != null)
            put(n, ++previous);
    }
}
