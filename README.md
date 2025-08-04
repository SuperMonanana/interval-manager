# Interval Manager - Mengyan Li

A Java library for managing a collection of time intervals,
providing a way to insert new intervals and automatically merge overlapping and adjacent ones.

## Project Description

The Interval Manager is a utility for handling time intervals(integers). It provides functionality to:
- Add intervals (automatically merging overlapping or adjacent intervals)
- Remove intervals (splitting or trimming existing intervals as needed)
- Retrieve the current list of merged intervals in sorted order

## Setup Instructions

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher

### Installation

Build the project with Maven:
```bash
mvn clean install
```

### Running Tests

Run the tests using Maven:
```bash
mvn test
```

## Usage Examples

### Basic Usage

```java
IntervalManager manager = new IntervalManager();

manager.addInterval(new int[]{1, 3});
manager.addInterval(new int[]{5, 7});
manager.addInterval(new int[]{2, 6});

manager.getIntervals(); // Result: [[1, 7]]

manager.removeInterval(new int[]{3, 5});
manager.getIntervals(); // Result: [[1, 3], [5, 7]]
```
This example is included in the main method.

## Tradeoffs and Limitations

### Known Limitations
- Only supports integer intervals, but it can be easily extended to double if necessary
- Not thread-safe - concurrent modifications may lead to unexpected results
- Not optimised for large datasets

### Next Steps
- Add thread safety
- Performance optimizations for large datasets
- Other functionality. e.g., Initiliasing with some intervals, reset, etc.

More details about design details, tradeoffs and assumptions are documented [here](DESIGN.md)