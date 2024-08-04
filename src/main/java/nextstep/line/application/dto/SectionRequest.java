package nextstep.line.application.dto;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
