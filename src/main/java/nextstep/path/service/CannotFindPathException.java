package nextstep.path.service;

public class CannotFindPathException extends RuntimeException{
    public CannotFindPathException(String msg) {
        super(msg);
    }
}
