package nextstep.path.application;

import nextstep.path.domain.ShortestPath;
import nextstep.path.application.dto.PathsResponse;
import nextstep.line.domain.Section;
import nextstep.line.domain.SectionRepository;
import nextstep.path.application.exception.NotAddedEndToSectionException;
import nextstep.path.application.exception.NotAddedStartToSectionException;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.application.exception.NotExistStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {
    private StationRepository stationRepository;

    private SectionRepository sectionRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathsResponse findShortestPaths(Long source, Long target) {
        Station start = lookUpStationBy(source);
        Station end = lookUpStationBy(target);

        List<Section> sections = sectionRepository.findAll();
        validateContains(sections, start, end);
        ShortestPath shortestPath = ShortestPath.from(sections);

        return new PathsResponse(shortestPath.getDistance(start, end), createStationResponses(shortestPath.getStations(start, end)));
    }

    private Station lookUpStationBy(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(NotExistStationException::new);
    }

    public void validateContains(List<Section> sections, Station start, Station end) {
        boolean containsStart = containsStationToSections(sections, start);
        boolean containsEnd = containsStationToSections(sections, end);

        if (!containsStart) {
            throw new NotAddedStartToSectionException(start.getName());
        }
        if (!containsEnd) {
            throw new NotAddedEndToSectionException(end.getName());
        }
    }

    private static boolean containsStationToSections(List<Section> sections, Station station) {
        return sections.stream()
                .anyMatch(section -> containsStationToSection(station, section));
    }

    private static boolean containsStationToSection(Station station, Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        return upStation.equals(station) || downStation.equals(station);
    }

    private List<StationResponse> createStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
