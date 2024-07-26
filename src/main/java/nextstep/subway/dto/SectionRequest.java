package nextstep.subway.dto;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public void setUpStationId(Long upStationId) {
        this.upStationId = upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public void setDownStationId(Long downStationId) {
        this.downStationId = downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
