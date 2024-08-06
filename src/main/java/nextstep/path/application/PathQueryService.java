package nextstep.path.application;

import nextstep.exceptions.ErrorMessage;
import nextstep.path.exceptions.PathNotFoundException;
import nextstep.path.payload.ShortestPathResponse;
import nextstep.path.repository.PathResolver;
import nextstep.station.domain.Station;
import nextstep.station.exception.NonExistentStationException;
import nextstep.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PathQueryService {

    private final StationRepository stationRepository;
    private final PathResolver pathResolver;

    public PathQueryService(final StationRepository stationRepository, final PathResolver pathResolver) {
        this.stationRepository = stationRepository;
        this.pathResolver = pathResolver;
    }

    public ShortestPathResponse getShortestPath(final Long source, final Long target) {
        assertStationExist(source, target);
        return pathResolver.get(source, target)
                .orElseThrow(() -> new PathNotFoundException(ErrorMessage.PATH_NOT_FOUND));
    }

    private void assertStationExist(final Long source, final Long target) {
        List<Long> sourceTarget = new ArrayList<>(Arrays.asList(source, target));
        List<Station> stations = stationRepository.findByIdIn(sourceTarget);
        if (stations.size() == sourceTarget.size()) {
            return;
        }
        List<Long> stationIds = stations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());
        //source, target과 db에서 조회한 데이터 비교해서 일치하지 않으면 에러
        sourceTarget.removeAll(stationIds);
        stationIds.forEach(id -> {
                    throw new NonExistentStationException(ErrorMessage.NON_EXISTENT_STATION, id);
                }
        );
    }

}
