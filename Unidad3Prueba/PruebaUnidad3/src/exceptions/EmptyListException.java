package exceptions;

public class EmptyListException extends Exception{

    public EmptyListException() {
        super("This list is Empty");
    }
}
