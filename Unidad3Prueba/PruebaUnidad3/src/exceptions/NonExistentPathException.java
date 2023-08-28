package exceptions;

public class NonExistentPathException extends Exception{

    public NonExistentPathException(){
        super("There is no path to node");
    }
    public NonExistentPathException(String msg){
        super(msg);
    }

}
