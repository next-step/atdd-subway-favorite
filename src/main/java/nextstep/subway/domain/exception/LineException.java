package nextstep.subway.domain.exception;

import nextstep.exception.DomainException;

public class LineException extends DomainException {

    public static final String ALREADY_REGISTERED_STATION_EXCEPTION = "이미 해당 노선에 등록되어있는 %s역은 새로운 구간의 하행역이 될 수 없습니다.";
    public static final String NOT_EXIST_SECTION = "노선에 등록된 구간이 없습니다.";
    public static final String NOT_ADDABLE_SECTION = "노선에 등록할 수 없는 구간입니다.";
    public static final String NOT_REMOVE_EXCEPTION = "노선을 삭제할 수 없습니다.";
    public static final String NOT_EXIST_STATION = "노선에 등록되지 않은 역입니다.";

    public LineException(String message) {
        super(message);
    }

    public static class AlreadyRegisteredStationException extends LineException {

        public AlreadyRegisteredStationException(String alreadyRegisteredStation) {
            super(String.format(ALREADY_REGISTERED_STATION_EXCEPTION, alreadyRegisteredStation));
        }
    }

    public static class NotExistSectionException extends LineException {

        public NotExistSectionException() {
            super(NOT_EXIST_SECTION);
        }
    }

    public static class NotAddableSectionException extends LineException {

        public NotAddableSectionException() {
            super(NOT_ADDABLE_SECTION);
        }
    }

    public static class NotRemoveException extends LineException {

        public NotRemoveException() {
            super(NOT_REMOVE_EXCEPTION);
        }
    }

    public static class NotExistStationException extends LineException {

        public NotExistStationException() {
            super(NOT_EXIST_STATION);
        }
    }
}
