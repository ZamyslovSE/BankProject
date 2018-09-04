package web.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

public class UsernameTakenException extends Throwable {
    public UsernameTakenException(String s) {
    }

    public UsernameTakenException(String s, Throwable e) {
    }

    public UsernameTakenException(Throwable e) {
    }
}
