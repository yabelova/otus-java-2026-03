package ru.otus;

import java.util.*;

public class CollectionMethods {

    // Task 1: Swap elements in generic array
    public static <T> void arrayElementsSwap(T[] array, int index1, int index2) {
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    // Task 2: Convert generic array to ArrayList
    public static <T> ArrayList<T> arrayToArrayList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    // Task 3: Count words and frequency
    public static Map<String, Integer> countWords(String[] array) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : array) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        return map;
    }
}