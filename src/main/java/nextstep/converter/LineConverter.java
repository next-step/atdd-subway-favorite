package nextstep.converter;

import nextstep.line.dto.LineResponse;
import nextstep.line.entity.Line;
import nextstep.station.dto.StationResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LineConverter {

    private LineConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static LineResponse convertToLineResponseByLine(final Line line) {
        List<StationResponse> stationResponses = line.getSections().getSections().stream()
                .flatMap(section -> section.getStations().stream())
                .map(station -> StationResponse.of(station.getId(), station.getName()))
                .distinct()
                .collect(toList());

        return convertToLineResponseByLineAndStations(line, stationResponses);
    }

    public static LineResponse convertToLineResponseByLineAndStations(final Line line, final List<StationResponse> stationResponses) {
        LineResponse lineResponse = LineResponse.of(line.getId(), line.getName(), line.getColor(), line.getDistance());
        lineResponse.addStationResponses(stationResponses);

        return lineResponse;
    }

}

