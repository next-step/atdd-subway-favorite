package nextstep.subway.path.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class ShortestPath {

    private GraphPath path;

    public ShortestPath(DijkstraShortestPath dijkstraShortestPath, Long sourceId, Long targetId) {
        this.path = createPath(dijkstraShortestPath, sourceId, targetId);
    }

    private GraphPath createPath(DijkstraShortestPath dijkstraShortestPath, Long sourceId, Long targetId) {
        try {
            path = dijkstraShortestPath.getPath(sourceId, targetId);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("출발역과 종착역이 연결되어 있지 않습니다.");
        }

        if (path == null) {
            throw new IllegalArgumentException("출발역과 종착역이 연결되어 있지 않습니다.");
        }

        if (path.getWeight() == 0) {
            throw new IllegalArgumentException("출발역과 종착역이 같습니다.");
        }

        return path;
    }

    public List<Long> getVertexList() {
        return path.getVertexList();
    }

    public int getWeight() {
        return (int) path.getWeight();
    }
}
