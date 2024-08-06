package nextstep.path.service;

import nextstep.line.entity.Line;
import nextstep.line.service.LineService;
import nextstep.path.dto.PathResponse;
import nextstep.station.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathFinder {

    private StationService stationService;
    private LineService lineService;
    private PathService pathService;

    public PathFinder(StationService stationService, LineService lineService, PathService pathService) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathService = pathService;
    }

    @Transactional(readOnly = true)
    public PathResponse retrieveStationPath(final Long source, final Long target) {
        validateStationExist(source, target);
        List<Line> lineList = lineService.getAllLines();
        return pathService.findPath(source, target, lineList);
    }

    private void validateStationExist(final Long source, final Long target) {
        stationService.getStationByIdOrThrow(source);
        stationService.getStationByIdOrThrow(target);
    }

}

