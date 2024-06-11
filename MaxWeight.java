public class MaxWeight {
    public static int locateLeafNode(Graph graph) {
        for (int i = 0; i < graph.numberVertices; i++) {
            Graph.LinkedList adjacencyList = graph.adjacencyList[i];
            // Check if the node has only one neighbor (leaf node)
            if (adjacencyList.head != null && adjacencyList.head.next == null) {
                return i;
            }
        }
        return -1;
    }
    public static int maxWeightChain(Graph graph) {
        int startLeaf = locateLeafNode(graph);
        boolean[] visited = new boolean[graph.numberVertices];
        visited[startLeaf] = true;
        int in = graph.vertexWeights[startLeaf];
        int[] result = chainDFS(graph, graph.adjacencyList[startLeaf].head.vertex, visited, in, 0, 0, 0);
        return Math.max(result[0], Math.max(result[1], result[2]));
    }

    public static int[] chainDFS(Graph graph, int starter, boolean[] visited, int in, int out1, int out2, int l1_out2_sum) {
        if (graph.adjacencyList[starter].head != null && graph.adjacencyList[starter].head.next == null) {
            int weight = graph.vertexWeights[starter];
            return new int[]{weight + Math.max(out1, out2), in, out1, l1_out2_sum};
        }

        visited[starter] = true;
        int new_in = graph.vertexWeights[starter] + Math.max(out1, out2);
        int new_out1 = in;
        int new_out2 = out1;

        Graph.Node curr = graph.adjacencyList[starter].head;
        while (curr != null && visited[curr.vertex]) {
            curr = curr.next;
        }

        return chainDFS(graph, curr.vertex, visited, new_in, new_out1, new_out2, l1_out2_sum);
    }

    public static int maxWeightTree(Graph G) {
        int[][] nodeInfoArray = new int[G.numberVertices][4];
        treeExplore(G, -1, 0, nodeInfoArray);
        return nodeInfoArray[0][0];
    }

    public static void treeExplore(Graph tree, int parent, int currentNode, int[][] nodeInfoArray) {
        int[] neighbors = getAdjacentNodes(currentNode, tree);
        if (neighbors.length == 1) {
            int[] leafInfo = new int[]{0, tree.vertexWeights[currentNode], 0, 0};
            nodeInfoArray[currentNode] = leafInfo;
        }
        int[] currentNodeInfo = new int[]{0,0,0,0};
        for (int neighbor: neighbors) {
            if (neighbor != parent) {
                treeExplore(tree, currentNode, neighbor, nodeInfoArray);

                int[] neighborInfo = nodeInfoArray[neighbor];

                int[] currentInfo = nodeInfoArray[currentNode];

                currentNodeInfo[0] = Math.max(currentInfo[0], Math.max(
                        neighborInfo[0],
                        Math.max(
                                currentInfo[1] + Math.max(neighborInfo[2], neighborInfo[3]),
                                Math.max(currentInfo[2] + Math.max(neighborInfo[1], neighborInfo[2]), currentInfo[3] + neighborInfo[1])
                        )
                ));

                currentNodeInfo[1] = Math.max(currentInfo[1], tree.vertexWeights[currentNode] + Math.max(neighborInfo[2], neighborInfo[3]));
                currentNodeInfo[2] = Math.max(currentInfo[2], neighborInfo[1]);
                currentNodeInfo[3] = Math.max(currentInfo[3], neighborInfo[2]);
                nodeInfoArray[currentNode] = currentNodeInfo;
            }
        }
    }

    public static int[] getAdjacentNodes(int node, Graph tree) {
        int[] adjacentNodes = new int[tree.getLength(node)];
        Graph.Node curr = tree.adjacencyList[node].head;
        int i = 0;
        while (curr != null) {
            adjacentNodes[i] = curr.vertex;
            i++;
            curr = curr.next;
        }
        return adjacentNodes;
    }

}
