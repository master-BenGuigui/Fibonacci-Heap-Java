/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 */

public class FibonacciHeap {
    public HeapNode min; // Pointer to the minimal node
    public int sizeVar; // Number of elements in the heap
    public int totalLinksVar = 0; // Number of links in the heap
    public int totalCutsVar = 0;  // Number of cuts in the heap
    public int numTreesVar;

    public FibonacciHeap() {
        this.min = null;
        this.sizeVar = 0;
        this.numTreesVar = 0;
    }

    public HeapNode insert(int key, String info) {
        HeapNode node = new HeapNode(key, info);
        node.next = node;
        node.prev = node;

        if (this.min == null) {
            this.min = node;
        } else {
            insertNodeToRootList(node, this.min);
            if (key < this.min.key) {
                this.min = node;
            }
        }

        this.numTreesVar++;
        this.sizeVar++;
        return node;
    }

    public HeapNode findMin() {
        return this.min;
    }

    public void deleteMin() {
        if (this.min == null) return;

        HeapNode oldMin = this.min;
        if (oldMin.child != null) {
            HeapNode child = oldMin.child;
            do {
                child.parent = null;
                this.totalCutsVar++;
                child = child.next;
            } while (child != oldMin.child);
            mergeCircularLists(this.min, oldMin.child);
            this.numTreesVar += oldMin.rank;
        }

        this.min = oldMin.next;
        removeNodeFromCircularList(oldMin);
        this.sizeVar--;
        this.numTreesVar--;

        if (this.sizeVar == 0) {
            this.min = null;
            return;
        }

        consolidate();
    }

    public void decreaseKey(HeapNode x, int diff) {
        if (x == null) return;

        x.key -= diff;
        HeapNode parent = x.parent;

        if (parent != null && x.key < parent.key) {
            cut(x, parent);
            cascadingCut(parent);
        }

        if (x.key < this.min.key) {
            this.min = x;
        }
    }

    public void delete(HeapNode x) {
        if (x == null || this.min == null) return;

        if (x == this.min) {
            deleteMin();
            return;
        }

        HeapNode oldMin = this.min;
        decreaseKey(x, x.key - oldMin.key + 1);

        if (x.child != null) {
            HeapNode child = x.child;
            do {
                child.parent = null;
                child = child.next;
            } while (child != x.child);
            mergeCircularLists(this.min, x.child);
            this.numTreesVar += x.rank;
        }

        removeNodeFromCircularList(x);
        this.sizeVar--;
        this.numTreesVar--;
        this.min = oldMin;
    }

    public int totalLinks() {
        return this.totalLinksVar;
    }

    public int totalCuts() {
        return this.totalCutsVar;
    }

    public void meld(FibonacciHeap heap2) {
        if (heap2 == null || heap2.min == null) return;

        this.totalLinksVar += heap2.totalLinks();
        this.totalCutsVar += heap2.totalCuts();

        if (this.min == null) {
            this.min = heap2.min;
            this.sizeVar = heap2.sizeVar;
            this.numTreesVar = heap2.numTreesVar;
        } else {
            mergeCircularLists(this.min, heap2.min);
            if (heap2.min.key < this.min.key) {
                this.min = heap2.min;
            }
            this.sizeVar += heap2.sizeVar;
            this.numTreesVar += heap2.numTreesVar;
        }

        heap2.min = null;
        heap2.sizeVar = 0;
    }

    public int size() {
        return this.sizeVar;
    }

    public int numTrees() {
        return this.numTreesVar;
    }

    // =========================
    // Helper Functions
    // =========================

    private void consolidate() {
        if (this.min == null) return;

        int rootCount = numTrees();
        HeapNode[] rootArray = new HeapNode[rootCount];
        HeapNode current = this.min;

        for (int i = 0; i < rootCount; i++) {
            rootArray[i] = current;
            current = current.next;
        }

        int MAX_RANK = (int)(Math.floor(Math.log(sizeVar) / Math.log(2.0))) + 1;
        HeapNode[] rankTable = new HeapNode[MAX_RANK];

        for (HeapNode node : rootArray) {
            if (node == null) continue;
            int rank = node.rank;

            while (rankTable[rank] != null) {
                HeapNode other = rankTable[rank];
                if (other.key < node.key) {
                    HeapNode temp = node;
                    node = other;
                    other = temp;
                }
                link(other, node);
                rankTable[rank] = null;
                rank++;
            }
            rankTable[rank] = node;
        }

        this.min = null;
        for (HeapNode node : rankTable) {
            if (node != null) {
                if (this.min == null) {
                    this.min = node;
                    node.next = node;
                    node.prev = node;
                } else {
                    insertNodeToRootList(node, this.min);
                    if (node.key < this.min.key) {
                        this.min = node;
                    }
                }
            }
        }
    }

    private void link(HeapNode y, HeapNode x) {
        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.next = y;
            y.prev = y;
        } else {
            insertNodeToRootList(y, x.child);
        }
        x.rank++;
        y.mark = false;
        this.numTreesVar--;
        this.totalLinksVar++;
    }

    private void cut(HeapNode x, HeapNode y) {
        removeNodeFromCircularList(x);
        if (y.child == x) {
            y.child = (x.next != x) ? x.next : null;
        }
        y.rank--;
        x.parent = null;
        x.mark = false;
        insertNodeToRootList(x, this.min);
        this.totalCutsVar++;
        this.numTreesVar++;
    }

    private void cascadingCut(HeapNode y) {
        HeapNode z = y.parent;
        if (z != null) {
            if (!y.mark) {
                y.mark = true;
            } else {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }

    private void mergeCircularLists(HeapNode a, HeapNode b) {
        if (a == null || b == null) return;

        HeapNode aNext = a.next;
        HeapNode bPrev = b.prev;

        a.next = b;
        b.prev = a;
        aNext.prev = bPrev;
        bPrev.next = aNext;
    }

    private void insertNodeToRootList(HeapNode node, HeapNode root) {
        node.prev = root;
        node.next = root.next;
        root.next.prev = node;
        root.next = node;
    }

    private void removeNodeFromCircularList(HeapNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = node;
        node.prev = node;
    }

    /**
     * Class representing a node in the Fibonacci Heap.
     */
    public static class HeapNode {
        public int key;
        public String info;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;
        public int rank;
        public boolean mark;

        public HeapNode(int key, String info) {
            this.key = key;
            this.info = info;
            this.child = null;
            this.next = this;
            this.prev = this;
            this.parent = null;
            this.rank = 0;
            this.mark = false;
        }
    }
}