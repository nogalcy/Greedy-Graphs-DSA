import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class Graph {
    public LinkedList[] adjacencyList;
    public int[] vertexWeights;
    public int numberVertices;
    public int numberEdges;

    public Graph(int verticeNumber) {
        this.numberVertices = verticeNumber;
        this.vertexWeights = new int[verticeNumber];
        this.adjacencyList = new LinkedList[verticeNumber];
        for (int i = 0; i < verticeNumber; i++) {
            this.adjacencyList[i] = new LinkedList();
        }
    }

    public Graph(int numVert, int numEdge) {
        this.numberVertices = numVert;
        this.numberEdges = numEdge;
        this.adjacencyList = new LinkedList[numVert];
        for (int i = 0; i < numVert; i++) {
            this.adjacencyList[i] = new LinkedList();
        }
    }

    public static Graph readVertexWeights(String filename) throws IOException {
        Scanner input = new Scanner(new File(filename));


        int numberVertices = input.nextInt();
        Graph graph = new Graph(numberVertices);
        input.nextLine();


        for (int i = 0; i < numberVertices; i++) {
            int weight = input.nextInt();
            graph.vertexWeights[i] = weight;
        }
        input.nextLine();


        for (int j = 0; j < numberVertices; j++) {
            Scanner lineScanner = new Scanner(input.nextLine());


            while (lineScanner.hasNextInt()) {
                int destination = lineScanner.nextInt();
                graph.addAdjacentNode(j, destination);
            }

            lineScanner.close();
        }

        input.close();
        return graph;
    }

    public static Graph readEdgeWeights(String filename) throws IOException {
        Scanner input = new Scanner(new File(filename));

        int numVert = input.nextInt();
        int numEdge = input.nextInt();

        Graph graph = new Graph(numVert, numEdge);
        input.nextLine();
        for (int i = 0; i < numEdge; i++) {
            int source = input.nextInt();
            int dest = input.nextInt();
            int weight = input.nextInt();
            weight = weight * -1;
            graph.addNormalEdge(source, dest, weight);
            input.nextLine();
        }

        input.close();
        return graph;
    }

    public static Graph readEdgeColors(String filename) throws IOException {
        Scanner input = new Scanner(new File(filename));

        int numVert = input.nextInt();
        int numEdge = input.nextInt();

        Graph graph = new Graph(numVert, numEdge);
        input.nextLine();
        for (int i = 0; i < numEdge; i++) {
            int source = input.nextInt();
            int dest = input.nextInt();
            int weight = input.nextInt();
            char color = input.next().charAt(0);
            graph.addColoredEdge(source, dest, weight, color);
            input.nextLine();
        }

        input.close();
        return graph;
    }

    public static class Node {
        int vertex;
        int edgeWeight;
        int destination;
        Node next;
        char color;

        public Node(int vertex) {
            this.vertex = vertex;
            this.next = null;
        }

        public Node(int source, int destination, int edgeWeight) {
            this.vertex = source;
            this.destination = destination;
            this.edgeWeight = edgeWeight;
            this.next = null;
        }

        public Node(int source, int destination, int edgeWeight, char color) {
            this.vertex = source;
            this.destination = destination;
            this.edgeWeight = edgeWeight;
            this.color = color;
            this.next = null;
        }

    }

    public void addAdjacentNode(int source, int destination) {
        this.adjacencyList[source].insertNode(destination);
    }

    public void addNormalEdge(int source, int destination, int edgeWeight) {
        this.adjacencyList[source].insertNode(source, destination, edgeWeight);
    }

    public void addColoredEdge(int source, int dest, int edgeWeight, char color) {
        this.adjacencyList[source].insertNode(source, dest, edgeWeight, color);
    }

    public static class LinkedList {
        Node head;

        public void insertNode(int vertex) {
            Node newNode = new Node(vertex);
            if (head == null) {
                head = newNode;
                newNode.next = null;
            }
            else {
                newNode.next = head;
                head = newNode;
            }
        }

        public void insertNode(int source, int destination, int edgeWeight) {
            Node newNode = new Node(source, destination, edgeWeight);
            if (head == null) {
                this.head = newNode;
                newNode.next = null;
            }
            else {
                newNode.next = head;
                head = newNode;
            }
        }

        public void insertNode(int source, int dest, int edgeWeight, char color) {
            Node newNode = new Node(source, dest, edgeWeight, color);
            if (head == null) {
                head = newNode;
                newNode.next = null;
            }
            else {
                newNode.next = head;
                head = newNode;
            }
        }

    }

    public int getLength(int node) {
        Node curr = adjacencyList[node].head;
        int count = 0;
        while (curr != null) {
            count++;
            curr = curr.next;
        }
        return count;
    }
    public int getWeight(int start, int end, LinkedList list) {
        Node curr = list.head;
        int weight = -1; // Default value if no matching edge is found

        while (curr != null) {
            if (curr.vertex == start && curr.destination == end) {
                // Check if the weight is smaller than the previously found weight
                if (weight == -1 || curr.edgeWeight < weight) {
                    weight = curr.edgeWeight;
                }
            }
            curr = curr.next;
        }

        return weight;
    }

}
