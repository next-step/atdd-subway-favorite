package atdd.path.application.dto;

import atdd.path.domain.Line;
import atdd.path.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponseView {
    private Long id;
    private String name;
    private String startTime;
    private String endTime;
    private int interval;
    private List<StationResponseView> stations;

    public LineResponseView() {
    }

    public LineResponseView(Long id, String name, String startTime, String endTime, int interval, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interval = interval;
        this.stations = StationResponseView.listOf(stations);
    }

    public static LineResponseView of(Line line) {
        return new LineResponseView(line.getId(), line.getName(), line.getStartTime().toString(), line.getEndTime().toString(), line.getIntervalTime(), line.getStations());
    }

    public static List<LineResponseView> listOf(List<Line> lines) {
        return lines.stream()
                .map(it -> new LineResponseView(it.getId(), it.getName(), it.getStartTime().toString(), it.getEndTime().toString(), it.getIntervalTime(), it.getStations()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getInterval() {
        return interval;
    }

    public List<StationResponseView> getStations() {
        return stations;
    }
}
