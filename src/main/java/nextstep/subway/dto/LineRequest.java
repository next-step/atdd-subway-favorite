package nextstep.subway.dto;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.upStationId = upStationId;
        this.color = color;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
