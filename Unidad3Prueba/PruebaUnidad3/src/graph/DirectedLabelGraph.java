package graph;

import exceptions.EmptyListException;
import exceptions.GraphSizeException;
import exceptions.IndexListException;
import list.MyLinkedList;

import java.util.HashMap;

public class DirectedLabelGraph<E> extends DirectedGraph {

    protected E[] labels;
    protected HashMap<E, Integer> dicVertices;

    public DirectedLabelGraph(int numVertices) {
        super(numVertices);
        this.labels = (E[]) new Object[numVertices +1];
        this.dicVertices = new HashMap<>(numVertices);
    }

    public boolean existLabelEdge(E i, E j) throws GraphSizeException {
        return this.edgeExist(getNumVertices(i), getNumVertices(j));
    }

    public int getNumVertices(E label){
        return dicVertices.get(label);
    }

    public E getLabel(int numVertices){
        return labels[numVertices];
    }

    public void addLabelEdge(E i, E j, double weight) throws GraphSizeException {
        this.addEdge(getNumVertices(i), getNumVertices(j), weight);
    }

    public void addLabelEdge(E i, E j) throws GraphSizeException {
        this.addEdge(getNumVertices(i), getNumVertices(j));
    }

    public MyLinkedList<Adjacency> adjacencyLabelMatrix(E i){
        return this.adjacencyList(getNumVertices(i));
    }

    public void labelVertex(int vertex, E label){
        labels[vertex] = label;
        dicVertices.put(label, vertex);
    }

    @Override
    public String toString() {
        StringBuilder graph = new StringBuilder("Graph \n");

        for(int i = 1; i <= numVertices() ; i++) {
            graph.append("Vertex[").append(getLabel(i)).append("]").append("\n");
            var tmpList = adjacencyList(i);

            graph.append(tmpList.isEmpty() ? "No Adjacencies" : "Adjacencies").append("\n");

            for(int j = 0; j < tmpList.size() ; j++) {
                try {
                    Adjacency tmp = tmpList.get(j);
                    graph.append("-> V").append(tmp.getPath()).append(" (").append(getLabel(tmp.getPath())).append(" Peso: ").append(tmp.getWeight()).append(")\n");
                } catch (IndexListException | EmptyListException e) {
                    throw new RuntimeException(e);
                }
            }
            graph.append("\n");
        }

        return graph.toString();
    }
}
