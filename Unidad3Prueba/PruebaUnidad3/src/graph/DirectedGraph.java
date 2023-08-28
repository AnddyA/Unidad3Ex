package graph;

import exceptions.EmptyListException;
import exceptions.GraphSizeException;
import exceptions.IndexListException;
import list.MyLinkedList;

public class DirectedGraph extends Graph {

    protected int numVertices;
    protected int numEdges;
    protected MyLinkedList<Adjacency>[] neighbors;

    public DirectedGraph(int numVertices) {

        this.numVertices = numVertices;

        this.numEdges = 0;

        this.neighbors = new MyLinkedList[numVertices + 1];

        for(int i = 0; i <= numVertices; i++) this.neighbors[i] = new MyLinkedList<>();

    }

    @Override
    public int numVertices() {
        return numVertices;
    }

    @Override
    public int numEdges() {
        return numEdges;
    }

    @Override
    public boolean edgeExist(int i, int j) throws GraphSizeException {

        if(i > numVertices || j > numVertices) throw new GraphSizeException();

        var tmpList = neighbors[i];

        for(int k = 0; k < tmpList.size() ; k++) {

            try {

                var tmp = tmpList.get(k);

                if(tmp.getPath() == j) return true;

            } catch (IndexListException | EmptyListException e) {
                throw new RuntimeException(e);
            }

        }

        return false;
    }

    @Override
    public double edgeWeight(int i, int j) throws GraphSizeException {

        if(i > numVertices || j > numVertices) throw new GraphSizeException();

        var tmpList = neighbors[i];

        for(int k = 0; k < tmpList.size() ; k++) {

            try {

                var tmp = tmpList.get(k);

                if(tmp.getPath() == j) return tmp.getWeight();

            } catch (IndexListException | EmptyListException e) {
                throw new RuntimeException(e);
            }

        }

        return Double.NaN;
    }

    @Override
    public void addEdge(int i, int j) throws GraphSizeException{
        addEdge(i, j , Double.NaN);
    }

    @Override
    public void addEdge(int i, int j, double weight) throws GraphSizeException {

        if(i > numVertices || j > numVertices) throw new GraphSizeException();

        if(edgeExist(i, j)) return;

        neighbors[i].add(new Adjacency(j, weight));

        numEdges++;

    }

    @Override
    public MyLinkedList<Adjacency> adjacencyList(int i) {
        return neighbors[i];
    }

}
