package subway.line.application.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;

@Getter
@Builder
public class SectionCreateRequest {
    private static final String DOWN_STATION_ID_NOT_BLANK_MESSAGE = "하행역은 반드시 지정해야 합니다.";
    private static final String UP_STATION_ID_NOT_BLANK_MESSAGE = "상행역은 반드시 지정해야 합니다.";
    private static final String DISTANCE_NOT_BLANK_MESSAGE = "구간의 거리는 반드시 지정해야 합니다.";

    @Min(value = 1, message = DOWN_STATION_ID_NOT_BLANK_MESSAGE)
    private Long downStationId;

    @Min(value = 1, message = UP_STATION_ID_NOT_BLANK_MESSAGE)
    private Long upStationId;

    @Min(value = 1, message = DISTANCE_NOT_BLANK_MESSAGE)
    private Long distance;
}
