package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class IntervalManagerTest {

    @Test
    void addInterval_WithDisjointIntervals_ShouldMaintainSorted() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{4, 5}); // add to empty
        manager.addInterval(new int[]{1, 2}); // add to the head
        manager.addInterval(new int[]{10, 11}); // add to the end
        manager.addInterval(new int[]{7, 8}); // add to the middle
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 2}, {4, 5}, {7, 8}, {10, 11}});
    }

    @Test
    void addInterval_WithAdjacentIntervals_ShouldMergeIntoSingleInterval() {
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
    void addInterval_WithOverlappingIntervals_ShouldMergeIntoSingleInterval() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 3});
        manager.addInterval(new int[]{2, 4});
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 4}});
    }

    @Test
    void addInterval_WithMultipleOverlappingIntervals_ShouldMergeOverlappingOnes() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 2});
        manager.addInterval(new int[]{4, 6}); // overlapping
        manager.addInterval(new int[]{7, 8}); // overlapping
        manager.addInterval(new int[]{9, 10}); // overlapping
        manager.addInterval(new int[]{12, 15});

        manager.addInterval(new int[]{5, 11});
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 2}, {4, 11}, {12, 15}});
    }

    @Test
    void addInterval_WithSmallerIntoLarger_ShouldKeepLargerInterval() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 5});
        manager.addInterval(new int[]{2, 3});

        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 5}});
    }

    @Test
    void addInterval_WithLargerIntoSmaller_ShouldKeepLargerInterval() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{2, 3});
        manager.addInterval(new int[]{1, 5});

        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 5}});
    }

    @Test
    void removeInterval_WhenNoOverlap_ShouldNotModifyIntervals() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 5});
        manager.removeInterval(new int[]{6, 10});
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 5}});
    }

    @Test
    void removeInterval_WhenRangeIsLarger_ShouldRemoveEntireInterval() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 5});
        manager.removeInterval(new int[]{0, 10});
        assertIntervalsEqual(manager.getIntervals(), new int[][]{});
    }

    @Test
    void removeInterval_WhenRangeOverlapsFirstHalf_ShouldTrimStart() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 5});
        manager.removeInterval(new int[]{0, 3});
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{3, 5}});
    }

    @Test
    void removeInterval_WhenRangeOverlapsSecondHalf_ShouldTrimEnd() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 5});
        manager.removeInterval(new int[]{3, 6});
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 3}});
    }

    @Test
    void removeInterval_WhenRemovingMiddle_ShouldSplitIntoTwoIntervals() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 10});
        manager.removeInterval(new int[]{3, 5});
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 3}, {5, 10}});
    }

    @Test
    void removeInterval_WhenRangeSpansMultipleIntervals_ShouldTrimAndRemove() {
        IntervalManager manager = new IntervalManager();
        manager.addInterval(new int[]{1, 3}); // no op
        manager.addInterval(new int[]{5, 7}); // should trim end
        manager.addInterval(new int[]{8, 9}); // should remove entirely
        manager.addInterval(new int[]{10, 15}); // should trim start
        manager.removeInterval(new int[]{6, 12});
        assertIntervalsEqual(manager.getIntervals(), new int[][]{{1, 3}, {5, 6}, {12, 15}});
    }

    private void assertIntervalsEqual(List<int[]> actual, int[][] expected) {
        assertEquals(expected.length, actual.size());
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual.get(i));
        }
    }
}