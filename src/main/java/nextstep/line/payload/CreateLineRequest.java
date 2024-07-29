package nextstep.line.payload;

public class CreateLineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public CreateLineRequest() {
    }

    public CreateLineRequest(final String name, final String color, final Long upStationId, final Long downStationId, final Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
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
