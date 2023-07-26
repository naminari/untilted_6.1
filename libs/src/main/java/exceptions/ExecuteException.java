package exceptions;

public class ExecuteException extends Exception{
    public ExecuteException(Throwable cause){
        super(cause);
    }
    public ExecuteException(String message){
        super(message);
    }
}
