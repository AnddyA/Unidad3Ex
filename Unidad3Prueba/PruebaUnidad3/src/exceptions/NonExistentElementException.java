package exceptions;

public class NonExistentElementException extends Exception{

    public NonExistentElementException() {
        super("This element doesn't exist in the list");
    }

}
