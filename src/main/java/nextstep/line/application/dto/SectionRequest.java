package nextstep.line.application.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SectionRequest {
    @NotNull
    @Min(1)
    private Long upStationId;
    @NotNull
    @Min(1)
    private Long downStationId;
    @Min(1)
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
