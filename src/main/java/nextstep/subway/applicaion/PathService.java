package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.exception.DuplicatedStationsException;
import nextstep.subway.exception.NotConnectSectionException;
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
        Path path = getPath(source, target);
        return PathResponse.of(path);
    }

    public Path getPath(Long source, Long target) {
        validateDuplicatedStations(source, target);
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        try {
            return subwayMap.findPath(upStation, downStation);
        } catch (IllegalArgumentException e) {
            throw new NotConnectSectionException();
        }
    }

    private void validateDuplicatedStations(Long source, Long target) {
        if (source.equals(target)) {
            throw new DuplicatedStationsException();
        }
    }
}
