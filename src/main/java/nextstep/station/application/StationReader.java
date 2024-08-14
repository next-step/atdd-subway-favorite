package nextstep.station.application;

import static nextstep.global.exception.ExceptionCode.NOT_FOUND_STATION;

import nextstep.global.exception.CustomException;
import nextstep.station.domain.Station;
import nextstep.station.infrastructure.StationRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class StationReader {
    private final StationRepository stationRepository;

    public StationReader(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station findById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new CustomException(NOT_FOUND_STATION));
    }
}
