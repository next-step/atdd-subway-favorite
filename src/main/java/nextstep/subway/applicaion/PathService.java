package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target) {
        checkSameId(source, target);
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        Path path = searchPath(upStation, downStation);
        return PathResponse.of(path);
    }

    private void checkSameId(Long source, Long target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수는 없습니다.");
        }
    }

    public Path searchPath(Station upStation, Station downStation) {
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        return subwayMap.findPath(upStation, downStation);
    }
}
