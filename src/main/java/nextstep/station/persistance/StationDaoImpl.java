package nextstep.station.persistance;

import nextstep.favorite.dao.StationDao;
import nextstep.station.domain.Station;
import nextstep.station.exception.StationNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class StationDaoImpl implements StationDao {

    private final StationRepository stationRepository;

    public StationDaoImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Station findStation(long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException("Station not exist : " + id));
    }
}
