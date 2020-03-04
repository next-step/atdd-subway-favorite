package atdd.path.domain;

import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public class Edge extends Item {
    private Long id;
    private Station sourceStation;
    private Station targetStation;
    private int distance;

    public Edge() {
    }

    public Edge(Long id, Station sourceStation, Station targetStation, int distance) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    public static Edge of(Station sourceStation, Station targetStation, int distance) {
        return new Edge(null, sourceStation, targetStation, distance);
    }

    public void checkSourceAndTargetStationIsSameWhenEdge() {
        if (isSameNameWithSourceAndTarget()) {
            throw new DuplicateKeyException("시작역과 종착역이 같을 수 없습니다.");
        }
    }

    public boolean isSameNameWithSourceAndTarget() {
        return getSourceStationName().equals(getTargetStationName());
    }

    private String getTargetStationName() {
        return targetStation.getName();
    }

    private String getSourceStationName() {
        return sourceStation.getName();
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public boolean hasStation(Station station) {
        return sourceStation.equals(station) || targetStation.equals(station);
    }

    public void checkBidirectionalSourceAndTarget() {
        checkLineInStationHasOppositeStation(getSourceStation(), getTargetStation());
        checkLineInStationHasOppositeStation(getTargetStation(), getSourceStation());
    }

    private void checkLineInStationHasOppositeStation(Station station, Station oppositeStation) {
        List<Line> linesInStation = station.getLines();
        linesInStation.stream()
                .filter(line -> line.getStations().contains(oppositeStation))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
