package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

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

    public List<Section> extractSections(List<Line> lines) {
        List<Section> lineSections = lines.stream()
                .flatMap(it -> it.getSections().stream())
                .collect(Collectors.toList());

        List<Section> resultSections = new ArrayList<>();
        Long upStationId = stationIds.get(0);
        for (Long downStationId : stationIds) {
            if (downStationId == upStationId) {
                continue;
            }
            Long finalUpStationId = upStationId;
            Section targetSection = lineSections.stream()
                    .filter(it -> isSameLineStation(finalUpStationId, downStationId, it))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            resultSections.add(targetSection);
            upStationId = extractPreStationId(targetSection, downStationId);
        }

        return resultSections;
    }

    private Long extractPreStationId(Section section, Long stationId) {
        return section.getUpStation().getId() == stationId ? section.getUpStation().getId() : stationId;
    }

    private boolean isSameLineStation(Long upStationId, Long downStationId, Section it) {
        return (it.getUpStation().getId() == upStationId && it.getDownStation().getId() == downStationId)
                || (it.getUpStation().getId() == downStationId && it.getDownStation().getId() == upStationId);
    }

    public List<Station> extractStations(List<Line> lines) {
        return stationIds.stream()
                .map(it -> extractStations(it, lines))
                .collect(Collectors.toList());
    }

    private Station extractStations(Long stationId, List<Line> lines) {
        return lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> it.getId() == stationId)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public int extractDistance(List<Line> lines) {
        return extractSections(lines).stream().mapToInt(it -> it.getDistance()).sum();
    }
}
