package nextstep.path;

import nextstep.section.Section;
import nextstep.section.SectionRepository;
import nextstep.station.Station;
import nextstep.station.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        List<Section> sections = sectionRepository.findAll();
        Station start = stationRepository.findById(source)
                .orElseThrow(() -> new EntityNotFoundException("해당 역이 존재하지 않습니다."));
        Station end = stationRepository.findById(target)
                .orElseThrow(() -> new EntityNotFoundException("해당 역이 존재하지 않습니다."));

        return new PathResponse(new Path(sections, start, end));
    }
}
