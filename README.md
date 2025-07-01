# Fibonacci Heap in Java

This is a full implementation of a Fibonacci Heap in Java, written as part of a data structures course.

The heap supports the following operations:

- `insert(key, info)`
- `findMin()`
- `deleteMin()`
- `decreaseKey(node, diff)`
- `delete(node)`
- `meld(otherHeap)`
- `size()`, `numTrees()`, `totalLinks()`, `totalCuts()`

The internal structure uses a circular doubly linked list and supports lazy consolidation for efficient amortized performance.

## Example usage

```java
FibonacciHeap heap = new FibonacciHeap();
heap.insert(10, "A");
heap.insert(5, "B");
heap.insert(7, "C");

System.out.println(heap.findMin().key); // 5

heap.deleteMin();
System.out.println(heap.findMin().key); // 7

-----

Technologies
Language: Java
IDE: Eclipse
-----

Structure
FibonacciHeap.java – Main heap implementation
HeapNode – Internal class representing nodes within the heap

------

Author
Yoav Ben Guigui,
Bachelor's student in Mathematics & Computer Science,Tel Aviv University

