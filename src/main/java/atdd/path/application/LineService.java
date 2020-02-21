package atdd.path.application;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.Edge;
import atdd.path.domain.Edges;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.repository.EdgeRepository;
import atdd.path.repository.LineRepository;
import atdd.path.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private EdgeRepository edgeRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, EdgeRepository edgeRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.edgeRepository = edgeRepository;
    }

    public void addEdge(Long lineId, Long sourceId, Long targetId, int distance) {
        Station source = stationRepository.findById(sourceId).orElseThrow(NoDataException::new);
        Station target = stationRepository.findById(targetId).orElseThrow(NoDataException::new);
        Line persistLine = lineRepository.findById(lineId).orElseThrow(NoDataException::new);

        Edge newEdge = Edge.of(source, target, distance);
        persistLine.addEdge(newEdge);

        source.getLines().add(persistLine);
        target.getLines().add(persistLine);

        edgeRepository.save(newEdge);
        lineRepository.save(persistLine);
        stationRepository.save(source);
        stationRepository.save(target);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Station station = stationRepository.findById(stationId).orElseThrow(NoDataException::new);
        Line persistLine = lineRepository.findById(lineId).orElseThrow(NoDataException::new);

        List<Edge> oldEdges = persistLine.getEdges();
        Edges edges = persistLine.removeStation(station);
        Edge newEdge = edges.getEdges().stream()
                .filter(it -> !oldEdges.contains(it))
                .findFirst().orElseThrow(RuntimeException::new);

        Edge deletedEdge = oldEdges.stream()
                                .filter(it -> !edges.getEdges().contains(it))
                                .findFirst().orElseThrow(RuntimeException::new);

        edgeRepository.deleteById(deletedEdge.getId());
        edgeRepository.save(newEdge);
    }
}
