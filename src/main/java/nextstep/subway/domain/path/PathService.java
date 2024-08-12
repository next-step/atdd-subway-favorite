package nextstep.subway.domain.path;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.path.dto.PathResponse;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.domain.station.StationService;
import nextstep.subway.domain.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(Long source, Long tartget) {
        List<Line> lines = lineRepository.findAll();
        List<Section> sections = lines.stream()
                .map(Line::getSections)
                .flatMap(List::stream)
                .collect(Collectors.toList());


        Station sourceStation = findByStationId(source);
        Station targetStation = findByStationId(tartget);

        PathFinder pathFinder = new PathFinder();
        var findResult = pathFinder.findPath(sourceStation, targetStation, sections).orElseThrow(() -> new IllegalArgumentException("요청한 경로를 찾을 수 없습니다."));
        List<Station> stations = findResult.getVertexList();
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        double weight = findResult.getWeight();

        return new PathResponse(stationResponses, (long) weight);
    }

    public Station findByStationId(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("조회할 역이 존재하지 않습니다."));
    }
}
