package nextstep.subway.domain.exception;

import nextstep.exception.DomainException;

public class StationException extends DomainException {

    private final static String NOT_FOUND_STATION = "해당 역이 존재하지 않습니다.";

    public StationException(String message) {
        super(message);
    }

    public static class StationNotFoundException extends StationException {

        public StationNotFoundException() {
            super(NOT_FOUND_STATION);
        }
    }
}
