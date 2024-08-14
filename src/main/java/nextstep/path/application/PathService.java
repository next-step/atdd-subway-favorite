package nextstep.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.line.infrastructure.SectionRepository;
import nextstep.station.domain.Station;
import nextstep.line.domain.Section;
import nextstep.path.domain.PathFinder;
import nextstep.station.infrastructure.StationRepository;
import nextstep.utils.Pair;
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
        Pair<Station, Long> sourceAndTarget = fetchSourceAndTarget(source, target);
        List<String> stationNames = pathFinder.getPath(findAllSections(), sourceAndTarget.getFirst(), sourceAndTarget.getSecond());
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
        Pair<Station, Long> sourceAndTarget = fetchSourceAndTarget(source, target);
        return pathFinder.getPathWeight(findAllSections(), sourceAndTarget.getFirst(), sourceAndTarget.getSecond());
    }

    public List<Section> findAllSections() {
        return sectionRepository.findAll();
    }

    private Pair<Station, Long> fetchSourceAndTarget(Long source, Long target) {
        return new Pair<>(source, target, stationRepository);
    }
}
