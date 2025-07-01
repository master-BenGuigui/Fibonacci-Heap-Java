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

-----

## ⏱️ Time Complexities (Amortized)

| Operation       | Complexity     |
|----------------|----------------|
| insert         | O(1)           |
| findMin        | O(1)           |
| deleteMin      | O(log n)       |
| decreaseKey    | O(1)           |
| delete         | O(log n)       |
| meld           | O(1)           |

- The heap uses a consolidation process after `deleteMin`, maintaining a logarithmic number of trees.
- `decreaseKey` triggers cascading cuts when a node becomes smaller than its parent.
-----

## 🧠 Design Highlights

- Nodes are managed via a **circular doubly linked root list**.
- Each node has a `rank`, and children are maintained in a cyclic list as well.
- The heap supports **marked nodes** to optimize cascading cuts in `decreaseKey`.
- `meld` joins two heaps in O(1) by linking root lists and updating min pointers.

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

