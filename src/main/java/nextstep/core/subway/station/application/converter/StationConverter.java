package nextstep.core.subway.station.application.converter;


import nextstep.core.subway.section.domain.Section;
import nextstep.core.subway.station.application.dto.StationResponse;
import nextstep.core.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class StationConverter {
    public static List<StationResponse> convertToStationResponses(List<Section> sections) {
        Set<StationResponse> stationResponses = new LinkedHashSet<>();

        sections.forEach(section -> {
            stationResponses.add(convertToStationResponse(section.getUpStation()));
            stationResponses.add(convertToStationResponse(section.getDownStation()));
        });

        return new ArrayList<>(stationResponses);
    }

    public static StationResponse convertToStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
