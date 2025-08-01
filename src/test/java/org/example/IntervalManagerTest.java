package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class IntervalManagerTest {

    @Test
    void testAddDisjointIntervals() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{4, 5}); // add to empty
        manager.addInterval(new int[]{1, 2}); // add to the head
        manager.addInterval(new int[]{10, 11}); // add to the end
        manager.addInterval(new int[]{7, 8}); // add to the middle
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 2}, {4, 5}, {7, 8}, {10, 11}});
    }

    @Test
    void testAddAdjacentIntervals() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 2});
        manager.addInterval(new int[]{3, 4});
        manager.addInterval(new int[]{2, 3}); // add to the middle
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 4}});

        manager.addInterval(new int[]{0, 1}); // add before
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{0, 4}});

        manager.addInterval(new int[]{4, 5}); // add after
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{0, 5}});
    }

    @Test
    void testAddOverlapIntervals() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 3});
        manager.addInterval(new int[]{2, 4});
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 4}});
    }

    @Test
    void testAddOverlapMultipleIntervals() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 2});
        manager.addInterval(new int[]{4, 5});
        manager.addInterval(new int[]{6, 8});
        manager.addInterval(new int[]{9, 10});

        manager.addInterval(new int[]{3, 7});
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 2}, {3, 8}, {9, 10}});
    }

    @Test
    void testAddNestedIntervals() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 5});
        manager.addInterval(new int[]{2, 3});

        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 5}});
    }

    @Test
    void testAddIncludedIntervals() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{2, 3});
        manager.addInterval(new int[]{1, 5});

        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 5}});
    }

    private void assertIntervalsEqual(List<int[]> actual, int[][] expected) {
        assertEquals(expected.length, actual.size());
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual.get(i));
        }
    }
}