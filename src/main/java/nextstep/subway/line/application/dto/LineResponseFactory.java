package nextstep.subway.line.application.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.application.dto.StationResponseFactory;

public class LineResponseFactory {

    private LineResponseFactory() {
    }

    public static LineResponse create(Line line) {
        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                StationResponseFactory.create(line));
    }

}
