package atdd.path.application;

import atdd.path.dao.LineDao;
import atdd.path.domain.Graph;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {
    private LineDao lineDao;

    public GraphService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public List<Station> findPath(long startId, long endId) {
        List<Line> lines = lineDao.findAll();
        Graph graph = new Graph(lines);
        return graph.getShortestDistancePath(startId, endId);
    }
}
