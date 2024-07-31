package nextstep.subway.service;

import nextstep.subway.dto.PathRequest;
import nextstep.subway.dto.PathResponse;
import nextstep.subway.entity.DijkstraShortestPathFinder;
import nextstep.subway.entity.Line;
import nextstep.subway.entity.PathFinderBuilder;
import nextstep.subway.entity.Station;
import nextstep.subway.exception.IllegalPathException;
import nextstep.subway.exception.NoSuchStationException;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse getPathOrThrow(PathRequest pathRequest) {
        if (pathRequest.getSource() == null|| pathRequest.getTarget() == null) {
            throw new IllegalPathException("경로를 찾을수 없습니다.");
        }

        Station sourceStation = getStation(pathRequest.getSource());
        Station targetStation = getStation(pathRequest.getTarget());

        List<Line> allLines = lineRepository.findAll();

        PathFinderBuilder pathFinderBuilder = DijkstraShortestPathFinder.searchBuilder();

        allLines.stream()
                .forEach(l -> pathFinderBuilder
                        .addVertex(l.getStations())
                        .addEdgeWeight(l.getSections().getSectionList())
                );

        return pathFinderBuilder
                .setSource(sourceStation)
                .setTarget(targetStation)
                .find();
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NoSuchStationException("존재하지 않는 역입니다."));
    }
}
