package nextstep.subway.applicaion;

import java.util.List;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineService lineService;
    private final StationRepository stationRepository;

    public PathService(LineService lineService, StationRepository stationRepository) {
        this.lineService = lineService;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long source, Long target) {
        Station upStation = stationRepository.findById(source).orElseThrow();
        Station downStation = stationRepository.findById(target).orElseThrow();
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation);

        return PathResponse.of(path);
    }
}
