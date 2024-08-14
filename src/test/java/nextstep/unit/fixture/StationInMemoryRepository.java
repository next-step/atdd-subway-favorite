package nextstep.unit.fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.station.domain.Station;
import nextstep.station.infrastructure.StationRepository;

public class StationInMemoryRepository extends PathFixture implements StationRepository {

    private final List<Station> stations;

    public StationInMemoryRepository() {
        setUp();
        this.stations = new ArrayList<>();
    }

    public List<Station> findAll() {
        return stations;
    }

    @Override
    public List<Station> findByNameIn(List<String> names) {
        List<Station> stations = findAll();
        return stations.stream()
                .filter(station -> names.contains(station.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Station save(Station station) {
        stations.add(station);
        return station;
    }

    @Override
    public Optional<Station> findById(Long id) {
        return stations.stream().filter(station -> station.getId().equals(id)).findAny();
    }

    @Override
    public void deleteById(Long id) {
        stations.removeIf(station -> station.getId().equals(id));
    }

    @Override
    public List<Station> findStationsByIdIn(List<Long> stationIds) {
        return stations.stream().filter(station -> stationIds.contains(station.getId())).collect(Collectors.toList());
    }
}
