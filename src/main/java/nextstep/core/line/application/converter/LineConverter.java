package nextstep.core.line.application.converter;


import nextstep.core.line.application.dto.LineRequest;
import nextstep.core.line.application.dto.LineResponse;
import nextstep.core.line.domain.Line;

import static nextstep.core.station.application.converter.StationConverter.convertToStationResponses;

public class LineConverter {

    public static Line convertToLine(LineRequest request) {
        return new Line(
                request.getName(),
                request.getColor());
    }

    public static LineResponse convertToLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                convertToStationResponses(line.getSortedAllSections()));
    }
}
