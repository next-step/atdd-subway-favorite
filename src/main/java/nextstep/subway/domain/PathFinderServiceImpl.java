package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;
import nextstep.common.exception.StationNotFoundException;
import nextstep.subway.infrastructure.SectionRepository;
import nextstep.subway.infrastructure.StationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PathFinderServiceImpl implements PathFinderService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    @Override
    public PathResult findPath(Long sourceId, Long targetId) {
        List<Section> allSections = sectionRepository.findAll();
        List<Station> allStations = stationRepository.findAll();

        Station sourceStation = stationRepository.findById(sourceId)
                .orElseThrow(() -> new StationNotFoundException(sourceId));
        Station targetStation = stationRepository.findById(targetId)
                .orElseThrow(() -> new StationNotFoundException(targetId));

        PathFinder pathFinder = PathFinderFactory.createPathFinder(allSections, allStations);
        return pathFinder.getShortestPath(sourceStation, targetStation);
    }

    @Override
    public boolean isValidPath(Long sourceId, Long targetId) {
        try {
            PathResult pathResult = findPath(sourceId, targetId);
            return !pathResult.getPathStations().isEmpty() && pathResult.getTotalDistance() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
