package nextstep.api.subway.applicaion.path;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.api.SubwayException;
import nextstep.api.subway.applicaion.path.dto.PathResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.api.subway.domain.line.LineRepository;
import nextstep.api.subway.domain.station.Station;
import nextstep.api.subway.domain.station.StationRepository;
import nextstep.api.subway.support.SubwayShortestPath;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        final var shortestPath = shortestPathOf(sourceId, targetId);

        return new PathResponse(
                StationResponse.toResponses(shortestPath.getStation()),
                shortestPath.getDistance()
        );
    }

    public void validateConnected(final Long sourceId, final Long targetId) {
        shortestPathOf(sourceId, targetId);
    }

    private SubwayShortestPath shortestPathOf(final Long sourceId, final Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new SubwayException(String.format("출발역과 도착역이 동일합니다: 출발역id=%d, 도착역id=%d", sourceId, targetId));
        }

        final var source = stationRepository.getById(sourceId);
        final var target = stationRepository.getById(targetId);

        return shortestPathOf(source, target);
    }

    private SubwayShortestPath shortestPathOf(final Station source, final Station target) {
        final var stations = stationRepository.findAll();
        final var sections = lineRepository.findAll().stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toUnmodifiableList());

        return SubwayShortestPath.builder(stations, sections)
                .source(source)
                .target(target)
                .build();
    }
}
