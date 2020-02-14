package atdd.path.application;

import atdd.path.dao.EdgeDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Edge;
import atdd.path.domain.Edges;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineService {
    private LineDao lineDao;
    private StationDao stationDao;
    private EdgeDao edgeDao;

    public LineService(LineDao lineDao, StationDao stationDao, EdgeDao edgeDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.edgeDao = edgeDao;
    }

    public void addEdge(Long lineId, Long sourceId, Long targetId, int distance) {
        Station source = stationDao.findById(sourceId);
        Station target = stationDao.findById(targetId);
        Line persistLine = lineDao.findById(lineId);

        Edge newEdge = Edge.of(source, target, distance);
        persistLine.addEdge(newEdge);

        edgeDao.save(lineId, newEdge);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Station station = stationDao.findById(stationId);
        Line persistLine = lineDao.findById(lineId);

        List<Edge> oldEdges = persistLine.getEdges();
        Edges edges = persistLine.removeStation(station);
        Edge newEdge = edges.getEdges().stream()
                .filter(it -> !oldEdges.contains(it))
                .findFirst().orElseThrow(RuntimeException::new);

        edgeDao.deleteByStationId(stationId);
        edgeDao.save(persistLine.getId(), newEdge);
    }
}
