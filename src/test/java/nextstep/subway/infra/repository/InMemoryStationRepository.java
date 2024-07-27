package nextstep.subway.domain.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.test.util.ReflectionTestUtils;

import nextstep.subway.domain.model.Station;

public class InMemoryStationRepository implements StationRepository {
    private static final String ID = "id";
    private Map<Long, Station> stations = new HashMap<>();
    private AtomicLong idGenerator = new AtomicLong();

    @Override
    public Station save(Station station) {
        if (station.getId() == null) {
            ReflectionTestUtils.setField(station, ID, idGenerator.incrementAndGet());
        }

        stations.put(station.getId(), station);
        return station;
    }

    @Override
    public List<Station> findAll() {
        return new ArrayList<>(stations.values());
    }

    @Override
    public void deleteById(Long id) {
        stations.remove(id);
    }

    @Override
    public Optional<Station> findById(Long stationId) {
        return Optional.ofNullable(stations.get(stationId));
    }
}