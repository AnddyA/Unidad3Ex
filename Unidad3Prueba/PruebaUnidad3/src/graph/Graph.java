package graph;

import exceptions.*;
import java.util.stream.IntStream;
import list.MyLinkedList;
import queue.MyQueue;

// For calculate each edge weight, I need to use PRIM algorithm

public abstract class Graph {
    public abstract int numVertices();

    public abstract int numEdges();

    public abstract boolean edgeExist(int i, int j) throws GraphSizeException;

    public abstract double edgeWeight(int i, int j) throws GraphSizeException;

    public abstract void addEdge(int i, int j) throws GraphSizeException;

    public abstract void addEdge(int i, int j, double weight) throws GraphSizeException;

    public abstract MyLinkedList<Adjacency> adjacencyList(int i);
    
    private final static double INF = Double.POSITIVE_INFINITY;

    @Override
    public String toString() {
        StringBuilder graph = new StringBuilder("Graph \n");

        for (int i = 1; i <= numVertices(); i++) {
            graph.append("Vertex[").append(i).append("]\n");
            var tmpList = adjacencyList(i);

            graph.append(tmpList.isEmpty() ? "No Adjacencies" : "Adjacencies").append("\n");

            for (int j = 0; j < tmpList.size(); j++) {
                try {
                    Adjacency tmp = tmpList.get(j);
                    graph.append("-> V").append(tmp.getPath()).append("(Weight: ").append(tmp.getWeight()).append(")\n");
                } catch (IndexListException | EmptyListException e) {
                    throw new RuntimeException(e);
                }
            }
            graph.append("\n");
        }

        return graph.toString();
    }

    private void transversalDepth(int v, boolean[] visited) {

        visited[v - 1] = true;

        var tmp = adjacencyList(v).toArray();

        if (tmp == null) return;

        for (var a : tmp) {

            var n = a.getPath();

            if (!visited[n - 1]) transversalDepth(n, visited);

        }

    }

    public boolean dfs(int v) {

        boolean[] visited = new boolean[numVertices()];

        transversalDepth(v, visited);
        
        return isConnected(visited);

    }

    public boolean bfs(int v) throws EmptyListException, IndexListException {

        boolean[] visited = new boolean[numVertices()];

        MyQueue<Integer> queue = new MyQueue<>();

        int n = 0;

        visited[v - 1] = true;

        queue.offer(v);

        while (queue.size() != 0) {

            v = queue.poll();

            System.out.print(v + " ");

            for (int i = 0; i < adjacencyList(v).size(); i++) {

                n = adjacencyList(v).get(i).getPath();

                if (!visited[n - 1]) {
                    visited[n - 1] = true;
                    queue.offer(n);
                }

            }

        }
        
        return isConnected(visited);

    }

    private boolean isConnected(boolean[] visited) {

        for (var vertex : visited) {
            if (!vertex) return false;
        }
        return true;
    }
    
    public MyLinkedList<Integer> bellmanFord(int src, int dest) {
        var graph = weightArr();

        int V = numVertices(), E = numEdges();

        double[] dist = new double[V];
        String[] label = new String[V];

        for (int i = 0; i < V; ++i) {
            dist[i] = INF;
            label[i] = "INF";
        }

        dist[src - 1] = 0;
        label[src - 1] = String.valueOf(src);

        for (int i = 1; i < V; ++i) {
            for (int j = 0; j < E; ++j) {
                int u = graph[j].getSrc() - 1;
                int v = graph[j].getDest() - 1;
                double weight = graph[j].getWeight();
                if (dist[u] != INF && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    label[v] = String.valueOf(u + 1);
                }
            }
        }

        for (int j = 0; j < E; ++j) {
            int u = graph[j].getSrc() - 1;
            int v = graph[j].getDest() - 1;
            double weight = graph[j].getWeight();
            if (dist[u] != INF && dist[u] + weight < dist[v]) {
                System.out.println("Graph contains negative weight cycle");
                return null;
            }
        }

        MyLinkedList<Integer> path = new MyLinkedList<>();

        if(label[dest - 1].equals("INF")) {
            System.out.println("No exist path");
            return null;
        }
        
        do {

            dest = Integer.parseInt(label[dest - 1]);
            
            path.addFirst(dest);
            
        } while (dest != src);
        
        return path;
    }
    
    private Edge[] weightArr() {

        Edge[] weights = new Edge[numEdges()];

        int init = 0;
        
        for (int i = 1; i <= numVertices(); i++) {
            init = addInfo(i, init, weights);
        }

        return weights;
    }

    private int addInfo(int i, int init, Edge[] weigth) {

        if (adjacencyList(i).isEmpty()) return init;

        var adjacencies = adjacencyList(i).toArray();

        for (var adj : adjacencies) {
            weigth[init] = new Edge(i, adj.getPath(), adj.getWeight());
            init++;
        }

        return init;
    }
    
    private double[][] weightMatrix() {

        var weights = new double[numVertices()][numVertices()];

        IntStream.range(0, numVertices()).forEach( i -> {
            IntStream.range(0, numVertices()).forEach( j -> {
                if (i == j) {
                    weights[i][j] = 0.0;
                } else {
                    try {
                        weights[i][j] = (edgeExist(i + 1, j + 1)) ? edgeWeight(i + 1, j + 1) : INF;
                    } catch (GraphSizeException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });

        return weights;
    }

    private String[][] successorMatrix(double[][] weights){

        String[][] successors = new String[numVertices()][numVertices()];

        IntStream.range(0, numVertices()).forEach( i -> {
            IntStream.range(0, numVertices()).forEach( j -> {
                if (weights[i][j] == INF || weights[i][j] == 0) {
                    successors[i][j] = "-";
                } else {
                    successors[i][j] = String.valueOf(j + 1);
                }
            });
        });

        return successors;
    }

    public MyLinkedList<Integer> floyd(int src, int dest) throws GraphCycleException, NonExistentPathException {

        MyLinkedList<Integer> path = new MyLinkedList<>();

        double[][] weightsMatrix = weightMatrix();

        String[][] successorsMatrix = successorMatrix(weightsMatrix);

        IntStream.range(0, numVertices()).forEach(intermediate ->
                IntStream.range(0, numVertices()).forEach(start ->
                        IntStream.range(0, numVertices()).forEach(end -> {
                            double weight = getWeightViaInterNode(weightsMatrix, start, intermediate, end);
                            if (weight < weightsMatrix[start][end]) {
                                weightsMatrix[start][end] = weight;
                                successorsMatrix[start][end] = successorsMatrix[start][intermediate];
                            }
                        })
                )
        );

        for (int i = 0; i < numVertices(); i++) {
            if (weightsMatrix[i][i] < 0) throw new GraphCycleException();
        }

        if(weightsMatrix[src - 1][dest - 1] == INF) throw new NonExistentPathException();

        do {

            src = Integer.parseInt(successorsMatrix[src - 1][dest - 1]);

            path.addFirst(src);

        } while (src != dest);

        return path;
    }

    private double getWeightViaInterNode(double[][] matrix, int start, int intermediate, int end) {
        return matrix[start][intermediate] == INF || matrix[intermediate][end] == INF
                ? INF : matrix[start][intermediate] + matrix[intermediate][end];
    }

}
