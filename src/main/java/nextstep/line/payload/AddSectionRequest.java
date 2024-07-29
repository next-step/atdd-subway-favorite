package nextstep.line.payload;

public class AddSectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Long distance;

    public AddSectionRequest() {
    }

    public AddSectionRequest(final Long upStationId, final Long downStationId, final Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
