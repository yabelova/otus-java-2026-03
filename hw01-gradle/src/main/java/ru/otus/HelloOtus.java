package ru.otus;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multisets;

public class HelloOtus {
    public static void main(String[] args) {
        var saturdaySales = ImmutableMultiset.of(
                "apple", "ananas", "orange", "apple", "orange", "banana", "apple",
                "apple", "apple", "kiwi", "orange", "banana", "grape", "apple"
        );
        var sundaySales = ImmutableMultiset.of(
                "apple", "apple", "kiwi", "grape", "banana", "kiwi", "banana",
                "apple", "orange", "orange", "apple"
        );

        var weekendSales = Multisets.sum(saturdaySales, sundaySales);
        var orderedWeekendSales = Multisets.copyHighestCountFirst(weekendSales);
        
        System.out.println(orderedWeekendSales);
    }
}