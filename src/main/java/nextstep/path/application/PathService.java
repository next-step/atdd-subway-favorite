package nextstep.path.application;

import nextstep.common.exception.NotExistStationException;
import nextstep.common.exception.SameStationException;
import nextstep.common.response.ErrorCode;
import nextstep.path.domain.ShortestPathFinderFactory;
import nextstep.section.domain.Section;
import nextstep.section.domain.SectionRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.path.application.dto.PathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse getShortestPath(Long source, Long target) {

        validateStation(source, target);
        List<Section> sectionList = sectionRepository.findAll();
        List<Station> stationList = stationRepository.findAll();
        var shortestPath = ShortestPathFinderFactory.createPathFinder(sectionList, stationList);

        return shortestPath.find(source, target, stationList);
    }

    private void validateStation(Long source, Long target) {
        if(source.equals(target)){
            throw new SameStationException(ErrorCode.SAME_STATION);
        }
        if(!stationRepository.existsById(source) || !stationRepository.existsById(target)){
            throw new NotExistStationException(ErrorCode.NOT_FOUND_STATION);
        }
    }

}
