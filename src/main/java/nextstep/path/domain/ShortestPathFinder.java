package nextstep.path.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.common.exception.PathNotFoundException;
import nextstep.common.response.ErrorCode;
import nextstep.station.domain.Station;
import nextstep.path.application.dto.PathResponse;
import nextstep.station.application.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class ShortestPathFinder {
    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph;

    public PathResponse find(Long source, Long target, List<Station> stationList) {

        DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        var path = shortestPath.getPath(source, target);
        if (path == null) {
            throw new PathNotFoundException(ErrorCode.NOT_FOUND_PATH);
        }

        List<Long> stationIds = path.getVertexList();
        List<StationResponse> stationMap = new ArrayList<>();
        stationIds.forEach(stationId -> stationMap.add(
                stationList.stream().filter(station -> station.getId().equals(stationId))
                        .findFirst().map(StationResponse::createResponse).orElseThrow()
        ));


        return new PathResponse(stationMap, (long) path.getWeight());
    }


}
