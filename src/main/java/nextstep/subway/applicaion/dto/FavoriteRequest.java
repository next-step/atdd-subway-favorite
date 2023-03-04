package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoriteRequest {

    @JsonProperty("source")
    private String sourceStationId;
    @JsonProperty
    private String targetStationId;

    private FavoriteRequest() {
    }

    public FavoriteRequest(final String sourceStationId, final String targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public String getSourceStationId() {
        return sourceStationId;
    }

    public String getTargetStationId() {
        return targetStationId;
    }
}
