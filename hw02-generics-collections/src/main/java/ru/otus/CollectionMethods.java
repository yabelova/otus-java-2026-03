package ru.otus;

import java.util.*;

public class CollectionMethods {

    /**
     * Swaps two elements in a generic array.
     *
     * @throws ArrayIndexOutOfBoundsException if the indexes are invalid
     * @throws NullPointerException           if the array is null
     */
    public static <T> void arrayElementsSwap(T[] array, int index1, int index2) {
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    /**
     * Converts a generic array to an ArrayList.
     *
     * @throws NullPointerException if the array is null
     */
    public static <T> ArrayList<T> arrayToArrayList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * Counts unique words and their frequency in the array.
     *
     * @return a map where keys are unique words and values are their counts
     * @throws NullPointerException if the array is null
     */
    public static Map<String, Integer> countWords(String[] array) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : array) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        return map;
    }
}