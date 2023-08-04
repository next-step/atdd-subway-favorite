package nextstep.service;

import nextstep.domain.Line;
import nextstep.domain.Station;
import nextstep.dto.PathResponse;
import nextstep.repository.LineRepository;
import nextstep.util.PathFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private StationService stationService;
    private LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository){
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse getPath(Long sourceId,Long targetId){

        Station sourceStation = stationService.findStation(sourceId);
        Station targetStation = stationService.findStation(targetId);

        List<Line> lineList = lineRepository.findAll();


        PathFinder pathFinder = new PathFinder();
        pathFinder.init(lineList);

        List<Station> path = pathFinder.getPath(sourceStation, targetStation);
        int distance = pathFinder.getDistance(sourceStation, targetStation);

        return PathResponse.createPathResponse(path, distance);

    }

    public void validatePath(Long sourceId,Long targetId){
        try {
            getPath(sourceId,targetId);
        } catch (Exception e) {
            throw new IllegalArgumentException("존재하지 않는 경로입니다.");
        }
    }
}
