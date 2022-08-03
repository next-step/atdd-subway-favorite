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
        validateDuplicatedStations(source, target);
        Path path = getPath(source, target);
        return PathResponse.of(path);
    }

    public Path getPath(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        SubwayMap subwayMap = getSubwayMap();
        return subwayMap.findPath(upStation, downStation);
    }

    public void validateDuplicatedStations(Long source, Long target) {
        if (source.equals(target)) {
            throw new DuplicatedStationsException();
        }
    }

    public void validatePath(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        SubwayMap subwayMap = getSubwayMap();
        subwayMap.validatePath(upStation, downStation);
    }

    private SubwayMap getSubwayMap() {
        return new SubwayMap(lineService.findLines());
    }
}
