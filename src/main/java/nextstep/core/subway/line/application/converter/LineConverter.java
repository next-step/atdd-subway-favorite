package nextstep.core.subway.line.application.converter;


import nextstep.core.subway.line.application.dto.LineRequest;
import nextstep.core.subway.line.application.dto.LineResponse;
import nextstep.core.subway.line.domain.Line;

import static nextstep.core.subway.station.application.converter.StationConverter.convertToResponses;

public class LineConverter {

    public static Line convertToLine(LineRequest request) {
        return new Line(
                request.getName(),
                request.getColor());
    }

    public static LineResponse convertToResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                convertToResponses(line.getSortedAllSections()));
    }
}
