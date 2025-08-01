package org.example;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class IntervalManager {
    LinkedList<Interval> intervals = new LinkedList<>(); // intervals sorted by start time

    /**
     * @param interval Adds a new interval. Automatically merge overlapping or adjacent intervals.
     */
    public void addInterval(int[] interval) {
        if (interval == null || interval.length != 2) {
            throw new IllegalArgumentException("Interval must have length 2");
        }

        Interval newInterval = new Interval(interval[0], interval[1]);
        ListIterator<Interval> iterator = intervals.listIterator();
        while (iterator.hasNext()) {
            Interval currentInterval = iterator.next();
            if (newInterval.end < currentInterval.start) {
                // Insert before current
                iterator.previous();
                iterator.add(newInterval);
                return;
            } else if (newInterval.start > currentInterval.end) {
                // No overlap or adjacent, keep going
                continue;
            } else {
                // Merge overlapping or adjacent intervals
                newInterval.start = Math.min(newInterval.start, currentInterval.start);
                newInterval.end = Math.max(newInterval.end, currentInterval.end);
                iterator.remove();
            }
        }

        intervals.add(newInterval);
    }

    /**
     * @return the list of merged intervals, sorted by start time.
     */
    public List<int[]> getIntervals() {
        return intervals.stream()
                .map(interval -> new int[]{interval.start, interval.end})
                .toList();
    }

    private static class Interval {
        int start;
        int end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

}
