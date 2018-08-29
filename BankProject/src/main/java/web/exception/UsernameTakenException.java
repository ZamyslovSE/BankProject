package web.exception;

import org.springframework.dao.DuplicateKeyException;

public class UsernameTakenException extends Throwable {
    public UsernameTakenException(String s) {
    }

    public UsernameTakenException(String s, Throwable e) {
    }
}
