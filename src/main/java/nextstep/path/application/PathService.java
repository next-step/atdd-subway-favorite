package nextstep.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.line.infrastructure.SectionRepository;
import nextstep.station.application.StationReader;
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
    private final StationReader stationReader;

    public PathService(PathFinder pathFinder, StationRepository stationRepository, SectionRepository sectionRepository,
            StationReader stationReader) {
        this.pathFinder = pathFinder;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.stationReader = stationReader;
    }

    public List<Station> getPath(Long source, Long target) {
        List<String> stationNames = pathFinder.getPath(findAllSections(), stationReader.findById(source),
                stationReader.findById(target));
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
        return pathFinder.getPathWeight(findAllSections(), stationReader.findById(source),
                stationReader.findById(target));
    }

    public List<Section> findAllSections() {
        return sectionRepository.findAll();
    }
}
