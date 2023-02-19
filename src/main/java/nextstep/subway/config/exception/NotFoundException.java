package nextstep.subway.config.exception;

import nextstep.subway.config.message.SubwayError;

public class NotFoundException extends RuntimeException {

    public NotFoundException(final SubwayError memberError) {
        super(memberError.getMessage());
    }
}