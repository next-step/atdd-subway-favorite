package nextstep.subway.domain;

import nextstep.subway.factory.ShortestPathFactory;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.strategy.ShortestPathStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {

    private final SectionRepository sectionRepository;

    public PathFinder(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Path findPath() {
        List<Section> sections = sectionRepository.findAll();
        ShortestPathStrategy shortestPathStrategy = new ShortestPathFactory().generateStrategy(ShortestPathType.DIJKSTRA, sections);

        return new Path(shortestPathStrategy);
    }
}
