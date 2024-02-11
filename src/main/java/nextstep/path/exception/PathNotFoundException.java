package nextstep.path.exception;


import nextstep.common.exception.NotFoundException;

public class PathNotFoundException extends NotFoundException {
    public PathNotFoundException() {
        super("path is unreachable");
    }
}
