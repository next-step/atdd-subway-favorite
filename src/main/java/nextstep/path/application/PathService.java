package nextstep.path.application;

import nextstep.line.application.LineProvider;
import nextstep.line.domain.Line;
import nextstep.path.application.dto.PathResponse;
import nextstep.path.application.dto.PathSearchRequest;
import nextstep.path.domain.Path;
import nextstep.path.domain.SubwayMap;
import nextstep.path.exception.PathNotFoundException;
import nextstep.station.domain.Station;
import nextstep.station.exception.StationNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineProvider lineProvider;

    public PathService(final LineProvider lineProvider) {
        this.lineProvider = lineProvider;
    }

    public PathResponse findShortestPath(final PathSearchRequest searchRequest) {
        final Path shortestPath = getPath(searchRequest).orElseThrow(PathNotFoundException::new);
        return PathResponse.from(shortestPath);
    }

    public boolean isInvalidPath(final PathSearchRequest searchRequest) {
        return getPath(searchRequest).isEmpty();
    }

    private Optional<Path> getPath(final PathSearchRequest searchRequest) {
        searchRequest.validate();

        final List<Line> allLines = lineProvider.getAllLines();
        final Map<Long, Station> stationMap = createStationMapFrom(allLines);
        final Station sourceStation = stationMap.computeIfAbsent(searchRequest.getSource(), throwStationNotFoundException());
        final Station targetStation = stationMap.computeIfAbsent(searchRequest.getTarget(), throwStationNotFoundException());

        final SubwayMap subwayMap = new SubwayMap(allLines);
        return subwayMap.findShortestPath(sourceStation, targetStation);
    }

    private Map<Long, Station> createStationMapFrom(final List<Line> allLines) {
        return allLines.stream()
                .flatMap(line -> line.getStations().stream())
                .distinct()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private static Function<Long, Station> throwStationNotFoundException() {
        return id -> {
            throw new StationNotExistException(id);
        };
    }
}
