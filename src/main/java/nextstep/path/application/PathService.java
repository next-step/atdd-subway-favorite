package nextstep.path.application;

import static nextstep.global.exception.ExceptionCode.NOT_FOUND_STATION;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.station.domain.Station;
import nextstep.station.infrastructure.StationRepository;
import nextstep.global.exception.CustomException;
import nextstep.line.domain.Section;
import nextstep.line.infrastructure.SectionRepository;
import nextstep.path.domain.PathFinder;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final PathFinder pathFinder;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(PathFinder pathFinder, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.pathFinder = pathFinder;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<Station> getPath(Long source, Long target) {
        SourceAndTargetStation sourceAndTargetStation = fetchSourceAndTarget(source, target);
        List<String> stationNames = pathFinder.getPath(findAllSections(), sourceAndTargetStation.getSource(), sourceAndTargetStation.getTarget());
        List<Station> stations = stationRepository.findByNameIn(stationNames);
        return toOrderByNames(stationNames, stations);
    }

    private List<Station> toOrderByNames(List<String> names, List<Station> stations) {
        return names.stream().flatMap(name -> stations
                        .stream()
                        .filter(station -> station.getName().equals(name))
                )
                .collect(Collectors.toList());
    }

    public double getPathWeight(Long source, Long target) {
        SourceAndTargetStation sourceAndTargetStation = fetchSourceAndTarget(source, target);
        return pathFinder.getPathWeight(findAllSections(), sourceAndTargetStation.getSource(), sourceAndTargetStation.getTarget());
    }

    public List<Section> findAllSections() {
        return sectionRepository.findAll();
    }

    private class SourceAndTargetStation {

        private final Station source;
        private final Station target;

        public SourceAndTargetStation(Long source, Long target) {
            this.source = findById(source);
            this.target = findById(target);
        }

        public Station getSource() {
            return source;
        }

        public Station getTarget() {
            return target;
        }

        private Station findById(Long id) {
            return stationRepository.findById(id)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));
        }
    }

    private SourceAndTargetStation fetchSourceAndTarget(Long source, Long target) {
        return new SourceAndTargetStation(source, target);
    }
}
