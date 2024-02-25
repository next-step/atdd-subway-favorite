package nextstep.path.service;

import nextstep.line.persistance.LineRepository;
import nextstep.path.domain.PathFinder;
import nextstep.path.domain.dto.PathsDto;
import nextstep.path.domain.dto.StationDto;
import nextstep.path.presentation.PathsResponse;
import nextstep.station.domain.Station;
import nextstep.station.exception.StationNotFoundException;
import nextstep.station.persistance.StationRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathsResponse searchPath(long source, long target) {
        try {
            PathFinder pathFinder = new PathFinder(lineRepository.findAll());
            PathsDto pathsDto = pathFinder.findPath(getStation(source), getStation(target));
            return new PathsResponse(pathsDto.getDistance(),
                    pathsDto.getPaths()
                            .stream()
                            .map(it -> new StationDto(it.getId(), it.getName()))
                            .collect(Collectors.toList())
            );
        } catch (IllegalArgumentException e) {
            CannotFindPathException ex = new CannotFindPathException("경로 탐색이 불가합니다");
            ex.initCause(e);
            throw ex;
        }
    }


    public boolean isConnectedPath(Station source, Station target) {
        PathFinder pathFinder = new PathFinder(lineRepository.findAll());
        return pathFinder.isConnected(getStation(source.getId()), getStation(target.getId()));
    }


    private Station getStation(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(Long.toString(stationId)));
    }

}
