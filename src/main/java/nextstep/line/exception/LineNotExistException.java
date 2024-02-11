package nextstep.line.exception;


import nextstep.common.exception.NotFoundException;

public class LineNotExistException extends NotFoundException {
    public LineNotExistException(final Long id) {
        super(String.format("Line is not exist - id : %s", id));
    }
}
