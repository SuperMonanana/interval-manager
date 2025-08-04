# Design of IntervalManager

## Assumptions

1. The interface adheres strictly to the requirements, prioritizing functionality over API design
2. Intervals are represented as integer array [start, end] where start ≤ end
3. The implementation is not necessarily to be thread-safe
4. Adjacent intervals are the intervals sharing a common endpoint without overlap
5. IllegalArgumentException will be thrown for invalid input (intervals are not properly formatted (length 2 arrays) or logically valid (start > end))
6. The InterviewManager starts empty and intervals are added one by one

## Interval Merging Approach

The IntervalManager uses a `LinkedList<Interval>` data structure to maintain non-overlapping intervals sorted by start time. This approach provides efficient insertion and removal operations needed for interval management.

### Algorithm 

   - For each new interval, we traverse the sorted list to find its correct position (O(n) time)
   - If the new interval comes before the current interval without overlap, we insert it at that position
   - If the new interval overlaps or is adjacent to an existing interval, we merge them by:
     - Taking the minimum of the start points
     - Taking the maximum of the end points
     - Removing the original interval from the list
   - We continue this process, potentially merging multiple intervals if the new merged interval overlaps with subsequent intervals
   - If we reach the end of the list without inserting, we add the interval at the end
   - The insertion/removal operation only takes constant time

### Edge Cases

The implementation carefully handles various edge cases:

1. Disjoint Intervals: Non-overlapping intervals are maintained in sorted order
2. Adjacent Intervals: Intervals like [1,2] and [2,3] are merged into [1,3]
3. Overlapping Intervals: Intervals like [1,3] and [2,4] are merged into [1,4]
4. Nested Intervals: When a smaller interval [2,3] is within a larger one [1,5], the larger interval is preserved
5. Multiple Overlaps: A new interval can trigger merging of multiple existing intervals
6. Point Intervals: Intervals where start equals end (e.g., [5,5]) are treated as valid intervals and merged according to the same rules:
   - Adjacent (e.g., [3,5] and [5,5]) will merge into a single interval ([3,5])
   - Contained within another interval (e.g., [5,5] within [4,6]) will be absorbed by the larger interval

Please see details in IntervalManagerTest

## Interval Removal (Subtraction)

The interval removal operation implements a "subtraction" of ranges, which can result in trimming, splitting, or complete removal of existing intervals.

### Algorithm

1. For each interval in the list, we check if it overlaps with the removal range (O(n) time)
2. Based on the overlap pattern, we perform one of four operations (O(1) time):
   - No Overlap: Leave the interval unchanged
   - Complete Overlap: Remove the entire interval
   - Partial Overlap at Start: Trim the start of the interval
   - Partial Overlap at End: Trim the end of the interval
   - Middle Overlap: Split the interval into two separate intervals

### Splitting Strategy

When the removal range falls entirely within an existing interval, we implement a splitting strategy:
1. Trim the original interval to end at the start of the removal range
2. Create a new interval starting at the end of the removal range and ending at the original end
3. Insert the new interval into the list

### Edge Cases

The implementation carefully handles various edge cases:

1. No Overlap: Removing [6,10] from [1,5] leaves [1,5] unchanged
2. Complete Removal: When the removal interval completely contains an existing interval, the entire interval is removed. Removing [0,10] from [1,5] results in an empty list
3. Partial Removal at Start: When the removal interval overlaps with the start of an existing interval, start is trimmed. Removing [0,3] from [1,5] results in [3,5]
4. Partial Removal at End: When the removal interval overlaps with the end of an existing interval, end is trimmed. Removing [3,6] from [1,5] results in [1,3]
5. Splitting: When the removal interval falls entirely within an existing interval, it's split into two intervals. Removing [3,5] from [1,10] results in [1,3] and [5,10]
6. Multiple Interval Removal: When the removal interval spans multiple existing intervals, some may be removed entirely while others are trimmed. With intervals [1,3], [5,7], [8,9], [10,15], removing [6,12] results in [1,3], [5,6], [12,15]
7. Point Interval Removal: Intervals where start equals end (e.g., [5,5]) are treated as valid intervals and removed according to the same rules:
   - Removing a point interval from another point interval results in complete removal. Removing [5,5] from [5,5] results in empty list
   - Removing a point from the middle of a larger interval splits it into two intervals. Removing [5,5] from [3,7] splits into [3,5] and [5,7]
   - Removing a point at the exact start or end of an interval has no effect. Removing [3,3] from [3,7] or [7,7] from [3,7] has no effect

Please see details in IntervalManagerTest

## Tradeoffs and Design Considerations

### Data Structure
Java's built-in `LinkedList` with `ListIterator` provides a balance of simplicity and efficiency. For higher performance requirements, a custom implementation of `ListNode` or `SkipList` could be considered.

### Mutability
The `Interval` class is implemented as a private static nested class with mutable fields to simplify the implementation of operations like trimming and splitting for interval removal. This design choice improves code readability and performance.
The internal `Interval` class is kept private to prevent external modification. If thread-safety is required, implementing `Interval` with `record` to maintain immutability could be a better choice

### Space and Time Complexity
`LinkedList<Interval>` takes O(n) space where n is the number of existing intervals.
For time:
  - Adding intervals: O(n) where n is the number of existing intervals
  - Removing intervals: O(n) where n is the number of existing intervals
  - Getting intervals: O(n) for the conversion from internal representation to the returned format

### Singleton Pattern Considerations
Currently `IntervalManager` is not implemented as a singleton. This design allows multiple independent interval collections to exist simultaneously, which is useful when different components of an application need to track different sets of intervals.

A singleton implementation would be appropriate when:
- The application requires a global, shared collection of intervals and centralized control of interval operations is needed to ensure consistency
- Memory usage is a concern and creating multiple instances would be wasteful