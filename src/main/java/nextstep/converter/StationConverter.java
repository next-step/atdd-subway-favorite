package nextstep.converter;

import nextstep.station.dto.StationResponse;
import nextstep.station.entity.Station;

public class StationConverter {

    private StationConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static StationResponse stationToStationResponse(final Station station) {
        return StationResponse.of(station.getId(), station.getName());
    }
}

