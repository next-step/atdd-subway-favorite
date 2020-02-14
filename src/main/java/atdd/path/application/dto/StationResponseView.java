package atdd.path.application.dto;

import atdd.path.domain.Line;
import atdd.path.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationResponseView {
    private Long id;
    private String name;
    private List<LineSimpleResponseView> lines;

    public StationResponseView() {
    }

    public StationResponseView(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationResponseView(Long id, String name, List<Line> lines) {
        this.id = id;
        this.name = name;
        this.lines = LineSimpleResponseView.listOf(lines);
    }

    public static StationResponseView of(Station station) {
        return new StationResponseView(station.getId(), station.getName(), station.getLines());
    }

    public static List<StationResponseView> listOf(List<Station> stations) {
        return stations.stream()
                .map(it -> new StationResponseView(it.getId(), it.getName()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<LineSimpleResponseView> getLines() {
        return lines;
    }
}
