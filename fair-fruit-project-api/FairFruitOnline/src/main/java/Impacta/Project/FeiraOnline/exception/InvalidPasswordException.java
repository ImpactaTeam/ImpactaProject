package Impacta.Project.FeiraOnline.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(){
        super("Invalid password");
    }
}