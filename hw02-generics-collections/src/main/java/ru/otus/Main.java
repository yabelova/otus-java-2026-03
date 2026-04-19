package ru.otus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        String[] strArray = {"dear sir/madam", "fire!", "fire!", "help me!"};

        System.out.println("\n-----TASK 1: Swap elements in generic array-----");
        System.out.println("Array before swap: " + Arrays.toString(strArray));
        CollectionMethods.arrayElementsSwap(strArray, 0, 2);
        System.out.println("Array after swap: " + Arrays.toString(strArray));

        System.out.println("\n-----TASK 2: Convert array to ArrayList-----");
        List<String> strArrayList = CollectionMethods.arrayToArrayList(strArray);
        System.out.println("ArrayList: " + strArrayList);

        System.out.println("\n-----TASK 3: Count words and print unique ones-----");
        String[] wordArray = {
                "o", "one", "one", "eight",
                "nine", "nine", "nine",
                "eight", "eight", "one", "nine", "nine",
                "nine", "one", "one", "nine",
                "seven", "two", "five",
                "three"
        };
        Map<String, Integer> wordMap = CollectionMethods.countWords(wordArray);
        System.out.println("Unique words and their frequency: " + wordMap);
    }
}

