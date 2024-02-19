package nextstep.subway.station.application.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponseFactory {

    private StationResponseFactory() {
    }

    public static List<StationResponse> create(Line line) {
        Sections sections = line.getSections();
        List<StationResponse> stationResponses = new ArrayList<>(create(sections.startStations()));
        stationResponses.add(create(sections.lastStation()));
        return stationResponses;
    }

    public static List<StationResponse> create(List<Station> stations) {
        return stations.stream()
                .map(StationResponseFactory::create)
                .collect(Collectors.toList());
    }

    public static StationResponse create(Station station) {
        return new StationResponse(station.getId(),
                station.getName());
    }

}
