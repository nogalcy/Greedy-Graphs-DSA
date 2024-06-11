public class ColorWalk {
    public static class WalkPair {
        char startColor;
        int walkWeight;

        WalkPair(int walkWeight, char startColor) {
            this.startColor = startColor;
            this.walkWeight = walkWeight;
        }

    }
    public static WalkPair[] colorWalk(Graph G, int start) {
        Graph G_prime = transformGraph(G);
        WalkPair[] prime_results = new WalkPair[G_prime.numberVertices];
        for (int i = 0; i < G_prime.numberVertices; i++) {
            prime_results[i] = new WalkPair(-1, '-');
        }
        prime_results[start * 3].walkWeight = 0;
        prime_results[(start * 3) + 1].walkWeight = 0;
        prime_results[(start * 3) + 2].walkWeight = 0;
        minHeapWalk minHeap = new minHeapWalk(G_prime.numberEdges);
        performDijkstra(3 * start, G_prime, prime_results, minHeap);
        performDijkstra((3 * start) + 1, G_prime, prime_results, minHeap);
        performDijkstra((3 * start) + 2, G_prime, prime_results, minHeap);
        WalkPair[] final_results = new WalkPair[G.numberVertices];
        int counter = 0;
        for (int j = 0; j < G_prime.numberVertices; j += 3) {

            WalkPair min = prime_results[j];
            if ((prime_results[j + 1].walkWeight < min.walkWeight && prime_results[j + 1].walkWeight != -1) || min.walkWeight == -1 || (prime_results[j + 1].walkWeight == min.walkWeight && compareColors(prime_results[j + 1].startColor, min.startColor) == -1)) {
                min = prime_results[j + 1];
            }
            if ((prime_results[j + 2].walkWeight < min.walkWeight && prime_results[j + 2].walkWeight != -1) || min.walkWeight == -1 || (prime_results[j + 2].walkWeight == min.walkWeight && compareColors(prime_results[j + 2].startColor, min.startColor) == -1)) {
                min = prime_results[j + 2];
            }
            final_results[counter] = min;
            counter++;
        }

        return final_results;
    }

    public static Graph transformGraph(Graph graph) {
        int newVertNum = graph.numberVertices * 3;

        Graph G_prime = new Graph(newVertNum, graph.numberEdges);
        for (int i = 0; i < graph.numberVertices; i++) {
            Graph.Node curr = graph.adjacencyList[i].head;
            while (curr != null) {
                char color = curr.color;
                int dest = curr.destination;
                int weight = curr.edgeWeight;
                if (color == 'R') {
                    G_prime.addNormalEdge(3 * i, (3 * dest) + 1, weight);
                }
                else if (color == 'G') {
                    G_prime.addNormalEdge((3 * i) + 1, (3 * dest) + 2, weight);
                }
                else if (color == 'B') {
                    G_prime.addNormalEdge((3 * i) + 2, 3 * dest, weight);
                }
                curr = curr.next;
            }
        }
        return G_prime;
    }

    public static void performDijkstra(int start, Graph G_prime, WalkPair[] prime_results, minHeapWalk heap) {
        addElementsToHeap(start, G_prime, heap, prime_results);
        while (!heap.isEmptyWalk()) {
            WalkPair minimum = heap.extractMinWalk();
            int current_node = heap.extractNeighbor();

            addElementsToHeap(current_node, G_prime, heap, prime_results);
        }
    }

    public static void addElementsToHeap(int start, Graph G_prime, minHeapWalk heap, WalkPair[] prime_results) {
        int[] adjacentEls = getAdjElements(G_prime, start);
        char source_col;
        for (int tracker = 0; tracker < adjacentEls.length; tracker++) {
            int neighbor = adjacentEls[tracker];
            int edgeWeight = G_prime.getWeight(start, neighbor, G_prime.adjacencyList[start]);
            if (prime_results[start].startColor == '-') {
                if (start % 3 == 0) {
                    source_col = 'R';
                }
                else if (start % 3 == 1) {
                    source_col = 'G';
                }
                else {
                    source_col = 'B';
                }
            }
            else {
                source_col = prime_results[start].startColor;
            }

            int weight_to_source = prime_results[start].walkWeight;
            int new_distance = weight_to_source + edgeWeight;

            if (new_distance < prime_results[neighbor].walkWeight || prime_results[neighbor].walkWeight == -1
                    || (new_distance == prime_results[neighbor].walkWeight && compareColors(source_col, prime_results[neighbor].startColor) == -1)) {
                prime_results[neighbor] = new WalkPair(new_distance, source_col);
                heap.insertWalk(new WalkPair(new_distance, source_col), neighbor);
            }
        }
    }

    public static int[] getAdjElements(Graph G_prime, int start) {
        int[] adj = new int[G_prime.getLength(start)];
        Graph.Node curr = G_prime.adjacencyList[start].head;
        int tracker = 0;
        while (curr != null) {
            adj[tracker] = curr.destination;
            tracker++;
            curr = curr.next;
        }
        return adj;
    }

    public static int compareColors(char color1, char color2) {

        // 0 identifies equal color order
        // 1 identifies leaving the order the same
        // -1 means to update to the tiebreak color

        if (color1 == color2) {
            return 0;
        }
        if (color1 == 'R') {
            return -1;
        } else if (color1 == 'G') {
            if (color2 == 'R') {
                return 1;
            }
            if (color2 == 'B') {
                return -1;
            }
        } else if (color1 == 'B') {
            if (color2 == 'R' || color2 == 'G') {
                return 1;
            }
        }
        return -5;
    }

    public static class minHeapWalk {
        WalkPair[] heap;
        int[] current_node_tracker;
        int size;

        public minHeapWalk(int size) {
            this.heap = new WalkPair[size];
            this.size = 0;
            this.current_node_tracker = new int[size];
        }

        public boolean isEmptyWalk() {
            return this.size == 0;
        }

        public void insertWalk(WalkPair walkPair, int neighbor) {
            this.heap[this.size] = walkPair;
            this.current_node_tracker[this.size] = neighbor;
            heapifyUpWalk(this.size);
            this.size++;
        }

        public WalkPair extractMinWalk() {
            if (!isEmptyWalk()) {
                WalkPair min = this.heap[0];
                this.heap[0] = this.heap[size - 1];
                this.size--;
                heapifyDownWalk(0);
                return min;
            }
            return null;
        }
        public int extractNeighbor() {
            int neighbor = this.current_node_tracker[0];
            this.current_node_tracker[0] = this.current_node_tracker[size];
            return neighbor;
        }

        public void heapifyUpWalk(int index) {
            while (index > 0) {
                int parentIndex = (index - 1) / 2;
                if (this.heap[index].walkWeight < this.heap[parentIndex].walkWeight) {
                    swapWalk(index, parentIndex);
                    swapNeighbor(index, parentIndex);
                    index = parentIndex;
                } else {
                    break;
                }
            }
        }

        public void heapifyDownWalk(int index) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < size) {
                if (this.heap[leftChild].walkWeight < this.heap[smallest].walkWeight) {
                    smallest = leftChild;
                }
            }
            if (rightChild < size) {
                if (this.heap[rightChild].walkWeight < this.heap[smallest].walkWeight) {
                    smallest = rightChild;
                }
            }
            if (smallest != index) {
                swapWalk(index, smallest);
                swapNeighbor(index, smallest);
                heapifyDownWalk(smallest);
            }
        }

        public void swapWalk(int i, int j) {
            WalkPair temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
        }
        public void swapNeighbor(int i, int j) {
            int temp = current_node_tracker[i];
            current_node_tracker[i] = current_node_tracker[j];
            current_node_tracker[j] = temp;
        }
    }

}
