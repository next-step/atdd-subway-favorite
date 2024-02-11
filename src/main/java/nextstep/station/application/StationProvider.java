package nextstep.station.application;

import nextstep.station.domain.Station;

public interface StationProvider {
    Station findById(Long id);
}
