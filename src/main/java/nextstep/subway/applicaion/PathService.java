package nextstep.subway.applicaion;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.SectionEdges;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {

    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(PathRequest request) {
        List<Line> lines = lineRepository.findAll();
        Map<Long, Station> stationById = lines.stream().flatMap(line -> line.getStations().stream())
            .collect(Collectors.toMap(Station::getId, station -> station, (a, b) -> a));

        SectionEdges edges = new SectionEdges(lines);

        PathFinder finder = new PathFinder(edges);
        Path path = finder.findShortedPath(request.getSource(), request.getTarget());
        return new PathResponse(path.getVertexList().stream().map(id -> {
            Station station = stationById.get(id);
            return new StationResponse(station.getId(), station.getName());
        }).collect(Collectors.toList()), path.getDistance());
    }
}
