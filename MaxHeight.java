public class MaxHeight {
    public static int maxHeight(Graph graph) {
        minHeap heap = new minHeap(graph.numberEdges);
        boolean[] visited = new boolean[graph.numberVertices];
        int startingVertex = 0;
        int maxWeight = Integer.MAX_VALUE;

        visited[startingVertex] = true;
        addEdgesToMinHeap(graph, startingVertex, heap, visited);
        while (!heap.isEmpty()) {
            Graph.Node minNode = heap.extractMin();
            if (!visited[minNode.destination]) {
                if ((minNode.edgeWeight * -1) < maxWeight) {
                    maxWeight = minNode.edgeWeight * -1;
                }
                visited[minNode.destination] = true;
                addEdgesToMinHeap(graph, minNode.destination, heap, visited);
            }
        }

        return maxWeight;
    }

    public static void addEdgesToMinHeap(Graph graph, int source, minHeap heap, boolean[] visited) {
        Graph.Node curr = graph.adjacencyList[source].head;
        while (curr != null) {
            if (!visited[curr.destination]) {
                heap.insert(curr);
            }
            curr = curr.next;
        }
    }

    public static class minHeap {
        Graph.Node[] heap;
        int size;

        public minHeap(int size) {
            this.heap = new Graph.Node[size];
            this.size = 0;
        }
        public boolean isEmpty() {
            return this.size == 0;
        }

        public void insert(Graph.Node node) {
            this.heap[this.size] = node;
            heapifyUp(this.size);
            this.size++;
        }

        public Graph.Node extractMin() {
            if (!isEmpty()) {
                Graph.Node min = this.heap[0];
                this.heap[0] = this.heap[size - 1];
                this.size--;
                heapifyDown(0);
                return min;
            }
            return null;
        }

        public void heapifyUp(int index) {
            while (index > 0) {
                int parentIndex = (index - 1) / 2;
                if (this.heap[index].edgeWeight < this.heap[parentIndex].edgeWeight) {
                    swap(index, parentIndex);
                    index = parentIndex;
                } else {
                    break;
                }
            }
        }

        public void heapifyDown(int index) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < size) {
                if (this.heap[leftChild].edgeWeight < this.heap[smallest].edgeWeight) {
                    smallest = leftChild;
                }
            }
            if (rightChild < size) {
                if (heap[rightChild].edgeWeight < heap[smallest].edgeWeight) {
                    smallest = rightChild;
                }
            }
            if (smallest != index) {
                swap(index, smallest);
                heapifyDown(smallest);
            }
        }
        public void swap(int i, int j) {
            Graph.Node temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
        }

    }


}
