package nextstep.subway.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SubwayDomainExceptionType {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR", "internal server error"),
    NOT_FOUND_STATION(HttpStatus.NOT_FOUND.value(), "NOT_FOUND_STATION", "not found station"),
    NOT_FOUND_STATION_ON_LINE(HttpStatus.NOT_FOUND.value(), "NOT_FOUND_STATION_ON_LINE", "not found station on line"),
    NOT_FOUND_LINE(HttpStatus.NOT_FOUND.value(), "NOT_FOUND_LINE", "not found line"),
    NOT_FOUND_LINE_SECTION(HttpStatus.NOT_FOUND.value(), "NOT_FOUND_LINE_SECTION", "not found line section"),
    NOT_FOUND_FAVORITE(HttpStatus.NOT_FOUND.value(), "NOT_FOUND_FAVORITE", "not found favorite"),
    INVALID_STATION(HttpStatus.BAD_REQUEST.value(), "INVALID_STATION", "invalid station"),
    INVALID_SECTION_SIZE(HttpStatus.BAD_REQUEST.value(), "INVALID_SECTION_SIZE", "invalid section size"),
    INVALID_SECTION_DISTANCE(HttpStatus.BAD_REQUEST.value(), "INVALID_SECTION_DISTANCE", "invalid section distance"),
    SOURCE_TARGET_SAME_STATION(HttpStatus.BAD_REQUEST.value(), "SOURCE_TARGET_SAME_STATION", "source & target are same station"),
    SOURCE_TARGET_NOT_CONNECTED(HttpStatus.BAD_REQUEST.value(), "SOURCE_TARGET_NOT_CONNECTED", "source & target are not connected"),
    ;

    private final int status;
    private final String name;
    private final String message;

    SubwayDomainExceptionType(int status, String name, String message) {
        this.status = status;
        this.name = name;
        this.message = message;
    }
}
