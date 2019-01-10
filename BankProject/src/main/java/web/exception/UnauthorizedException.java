package web.exception;

/**
 * Created by zdoba on 04.12.2018.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String s) {
        super(s);
    }
}
