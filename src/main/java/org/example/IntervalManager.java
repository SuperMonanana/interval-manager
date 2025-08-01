package org.example;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class IntervalManager {
    private final LinkedList<Interval> intervals = new LinkedList<>(); // intervals sorted by start time

    /**
     * @param interval Adds a new interval. Automatically merge overlapping or adjacent intervals.
     */
    public void addInterval(int[] interval) {
        validateInterval(interval);

        Interval toAdd = new Interval(interval[0], interval[1]);
        ListIterator<Interval> iterator = intervals.listIterator();
        while (iterator.hasNext()) {
            Interval current = iterator.next();
            if (toAdd.end < current.start) {
                // Insert before current
                iterator.previous();
                iterator.add(toAdd);
                return;
            } else if (toAdd.start > current.end) {
                // No overlap or adjacent, keep going
                continue;
            } else {
                // Merge overlapping or adjacent intervals
                toAdd.start = Math.min(toAdd.start, current.start);
                toAdd.end = Math.max(toAdd.end, current.end);
                iterator.remove();
            }
        }

        intervals.add(toAdd);
    }

    /**
     * @return the list of merged intervals, sorted by start time.
     */
    public List<int[]> getIntervals() {
        return intervals.stream()
                .map(interval -> new int[]{interval.start, interval.end})
                .toList();
    }

    /**
     * @param interval Removes a range from existing intervals, splitting or trimming as needed.
     */
    public void removeInterval(int[] interval) {
        validateInterval(interval);

        int removeStart = interval[0];
        int removeEnd = interval[1];

        ListIterator<Interval> iterator = intervals.listIterator();

        while (iterator.hasNext()) {
            Interval current = iterator.next();

            if (current.end < removeStart || current.start > removeEnd) {
                continue; // No overlap
            }

            if (removeStart <= current.start && removeEnd >= current.end) {
                // Remove entire current interval
                iterator.remove();
            } else if (removeStart <= current.start) {
                // Trim start
                current.start = removeEnd;
            } else if (removeEnd >= current.end) {
                // Trim end
                current.end = removeStart;
            } else {
                // Split into two intervals
                Interval right = new Interval(removeEnd, current.end);
                current.end = removeStart;
                iterator.add(right);
            }
        }
    }

    private static void validateInterval(int[] interval) {
        if (interval == null || interval.length != 2) {
            throw new IllegalArgumentException("Interval must have length 2");
        } else if (interval[0] > interval[1]) {
            throw new IllegalArgumentException("Start must be less than or equal to end");
        }
    }

    private static class Interval {
        private int start;
        private int end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

}
