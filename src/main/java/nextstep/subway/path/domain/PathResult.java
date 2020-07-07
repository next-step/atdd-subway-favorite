package nextstep.subway.path.domain;

import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathResult {
    private List<Long> stationIds;
    private double weight;

    public PathResult(List<Long> stationIds, double weight) {
        this.stationIds = stationIds;
        this.weight = weight;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public double getWeight() {
        return weight;
    }

    public List<LineStationResponse> extractLineStationResponses(List<LineResponse> lines) {
        List<LineStationResponse> lineStations = lines.stream()
                .flatMap(it -> it.getStations().stream())
                .collect(Collectors.toList());

        List<LineStationResponse> lineStationResponses = new ArrayList<>();
        Long preStationId = stationIds.get(0);
        for (Long stationId : stationIds) {
            if (stationId == preStationId) {
                continue;
            }
            Long finalPreStationId = preStationId;
            LineStationResponse lineStationResponse = lineStations.stream()
                    .filter(it -> isSameLineStation(stationId, finalPreStationId, it))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            lineStationResponses.add(lineStationResponse);
            preStationId = extractPreStationId(lineStationResponse, stationId);
        }

        return lineStationResponses;
    }

    private Long extractPreStationId(LineStationResponse lineStationResponse, Long stationId) {
        return lineStationResponse.getPreStationId() == stationId ? lineStationResponse.getPreStationId() : stationId;
    }

    private boolean isSameLineStation(Long stationId, Long preStationId, LineStationResponse it) {
        return (it.getPreStationId() == preStationId && it.getStation().getId() == stationId)
                || (it.getPreStationId() == stationId && it.getStation().getId() == preStationId);
    }

    public List<StationResponse> extractStationResponse(List<LineResponse> lines) {
        return stationIds.stream()
                .map(it -> extractStationResponse(it, lines))
                .collect(Collectors.toList());
    }

    private StationResponse extractStationResponse(Long stationId, List<LineResponse> lines) {
        return lines.stream()
                .flatMap(it -> it.getStations().stream())
                .map(it -> it.getStation())
                .filter(it -> it.getId() == stationId)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
