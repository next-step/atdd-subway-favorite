package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.exception.DuplicatedStationsException;
import nextstep.subway.exception.DisconnectedSectionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation);

        return PathResponse.of(path);
    }

    public void validateDuplicatedStations(Long source, Long target) {
        if (source.equals(target)) {
            throw new DuplicatedStationsException();
        }
    }

    public void validatePath(Station upStation, Station downStation) {
        if (getSubwayMap().isGraphPathNull(upStation, downStation)) {
            throw new DisconnectedSectionException();
        }
    }

    private SubwayMap getSubwayMap() {
        return new SubwayMap(lineService.findLines());
    }

}
