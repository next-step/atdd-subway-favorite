package nextstep.path.exceptions;

import nextstep.exceptions.BaseException;
import nextstep.exceptions.ErrorMessage;

public class PathNotFoundException extends BaseException {
    public PathNotFoundException(final ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
