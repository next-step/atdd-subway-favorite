package nextstep.subway.domain.line.dto;

public class LineCreateRequest {
    private String name;
    private String color;
    private Long upwardStationId;
    private Long downwardStationId;
    private int distance;

    public LineCreateRequest() {
    }

    public LineCreateRequest(String name, String color, Long upwardStationId, Long downwardStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upwardStationId = upwardStationId;
        this.downwardStationId = downwardStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpwardStationId() {
        return upwardStationId;
    }

    public Long getDownwardStationId() {
        return downwardStationId;
    }

    public int getDistance() {
        return distance;
    }
}
