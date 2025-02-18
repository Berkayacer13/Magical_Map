import java.util.ArrayList;

public class CustomHeap {
    private ArrayList<Node> heap; // ArrayList to hold heap elements

    // Constructor
    public CustomHeap() {
        this.heap = new ArrayList<>();
    }

    // Insert a new node into the heap
    public void insert(Node node) {
        heap.add(node);           // Add the new node at the end
        siftUp(heap.size() - 1);  // Adjust its position
    }

    // Remove and return the min (smallest distance node)
    public Node extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        Node min = heap.get(0);                     // Root element is the min
        Node last = heap.remove(heap.size() - 1);   // Remove the last element

        if (!heap.isEmpty()) {
            heap.set(0, last); // Move the last element to the root
            siftDown(0);      // Adjust the heap
        }

        return min;
    }

    // Peek at the min element without removing it

    // Check if the heap is empty
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Get the size of the heap
    public int getSize() {
        return heap.size();
    }

    // Sift up to maintain min-heap property
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            // Compare with parent
            if (heap.get(index).compareTo(heap.get(parentIndex)) < 0) { // Min-heap condition
                swap(index, parentIndex);
                index = parentIndex; // Move up to the parent's index
            } else {
                break;
            }
        }
    }

    // Sift down to maintain min-heap property
    private void siftDown(int index) {
        int leftChild;
        int rightChild;
        int smallest = index;

        while (true) {
            leftChild = 2 * index + 1;
            rightChild = 2 * index + 2;

            // Compare with left child
            if (leftChild < heap.size() &&
                    heap.get(leftChild).compareTo(heap.get(smallest)) < 0) {
                smallest = leftChild;
            }

            // Compare with right child
            if (rightChild < heap.size() &&
                    heap.get(rightChild).compareTo(heap.get(smallest)) < 0) {
                smallest = rightChild;
            }
            // If the smallest is still the current node, stop
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest; // Move to the smallest child
        }


    }
    // Swap two elements in the heap
    private void swap(int i, int j) {
        Node temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

}

