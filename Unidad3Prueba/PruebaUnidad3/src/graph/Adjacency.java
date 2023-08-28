package graph;

public class Adjacency {

    private int path;
    private double weight;

    public Adjacency(int path, double weight) {
        this.path = path;
        this.weight = weight;
    }
    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}
