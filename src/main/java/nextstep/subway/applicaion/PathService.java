package nextstep.subway.applicaion;

import nextstep.member.domain.FavoriteStations;
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
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        Path path = findPath(upStation, downStation);

        return PathResponse.of(path);
    }

    public FavoriteStations checkValidPathForFavorite(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        findPath(upStation, downStation);

        return new FavoriteStations(upStation, downStation);
    }

    private Path findPath(Station source, Station target) {
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        return subwayMap.findPath(source, target);
    }
}
