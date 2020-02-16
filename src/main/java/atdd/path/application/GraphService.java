package atdd.path.application;

import atdd.path.dao.LineDao;
import atdd.path.domain.Graph;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.repository.LineRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GraphService {
    private LineRepository lineRepository;

    public GraphService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<Station> findPath(long startId, long endId) {
        List<Line> lines = new ArrayList<>();
        lineRepository.findAll()
                      .forEach(lines::add);
        Graph graph = new Graph(lines);
        return graph.getShortestDistancePath(startId, endId);
    }
}
