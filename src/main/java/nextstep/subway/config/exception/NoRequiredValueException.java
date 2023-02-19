package nextstep.subway.config.exception;

import nextstep.subway.config.message.SubwayError;

public class NoRequiredValueException extends RuntimeException {

    public NoRequiredValueException(final SubwayError memberError) {
        super(memberError.getMessage());
    }
}
