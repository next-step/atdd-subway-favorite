package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.SectionRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final SectionRepository sectionRepository;

    public PathService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public PathResponse find(Long sourceId, Long targetId) {
        return PathFinder.find(sourceId, targetId, sectionRepository.findAll());
    }
}
