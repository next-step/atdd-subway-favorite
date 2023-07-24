package subway.line.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.line.domain.Section;
import subway.line.domain.SectionRepository;
import subway.station.domain.Station;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;

    public List<Section> findByStation(Station sourceStation, Station targetStation) {
        List<Section> sectionsByStation = sectionRepository.findByUpStationOrDownStation(sourceStation, sourceStation);
        List<Section> sectionsByTargetStation = sectionRepository.findByUpStationOrDownStation(targetStation, targetStation);
        sectionsByStation.addAll(sectionsByTargetStation);
        return sectionsByStation;
    }
}
