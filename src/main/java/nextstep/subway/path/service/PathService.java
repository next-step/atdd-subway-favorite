package nextstep.subway.path.service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.controller.dto.PathResponse;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final StationService stationService;
    private final LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse getPaths(Long sourceStationId, Long targetStationId) {
        validate(sourceStationId, targetStationId);

        Station source = stationService.findByStationId(sourceStationId);
        Station target = stationService.findByStationId(targetStationId);

        return new PathFinder().findPath(findAllSections(), source, target);
    }

    private List<Sections> findAllSections() {
        return lineRepository.findAll().stream()
            .map(Line::getSections)
            .collect(Collectors.toList());
    }

    private void validate(Long sourceStationId, Long targetStationId) {
        if (sourceStationId == null || targetStationId == null) {
            throw new PathException("출발역과 도착역 모두 필수값입니다.");
        }

        if (sourceStationId.equals(targetStationId)) {
            throw new PathException("출발역과 도착역이 동일합니다.");
        }
    }
}
