package web.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String s) {
        super(String.format("No user with login %s",s));
    }

    public UserNotFoundException(String s, Throwable e) {
        super(String.format("No user with login %s",s), e);
    }

    public UserNotFoundException(Throwable e) {
        super(e);
    }
}
