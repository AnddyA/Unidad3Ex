package exceptions;

public class GraphSizeException extends Exception{
    public GraphSizeException() {
        super("Size doesn't exist in the Graph");
    }

    public GraphSizeException(String msg) {
        super(msg);
    }
}
