package org.example;

import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Example for add and get
        IntervalManager manager = new IntervalManager();

        manager.addInterval(new int[]{1, 3});
        manager.addInterval(new int[]{5, 7});
        manager.addInterval(new int[]{2, 6});
        System.out.println("Intervals: " +
                manager.getIntervals().stream()
                        .map(Arrays::toString)
                        .toList()); // Output: [[1, 7]]

        // Example for remove
        IntervalManager removeManager = new IntervalManager();
        removeManager.addInterval(new int[]{1, 10});
        removeManager.removeInterval(new int[]{3, 5});
        System.out.println("Intervals: " +
                removeManager.getIntervals().stream()
                        .map(Arrays::toString)
                        .toList()); // Output: [[1, 3], [5, 10]
    }
}