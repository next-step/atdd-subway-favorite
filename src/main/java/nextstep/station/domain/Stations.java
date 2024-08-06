package nextstep.station.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Stations {

    private List<Station> stations;

    private Stations(List<Station> stations) {
        this.stations = stations;
    }

    public static Stations of(Station upStationId, Station downStationId) {
        return new Stations(new ArrayList<>(List.of(upStationId, downStationId)));
    }

    public void add(Station station) {
        stations.add(station);
    }

    public Station lastStation() {
        return stations.get(stations.size() - 1);
    }

    public List<Long> getStationIds() {
        return stations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());
    }

    public boolean contains(Station station) {
        return stations.contains(station);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stations stations1 = (Stations) o;
        return Objects.equals(stations, stations1.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations);
    }
}
