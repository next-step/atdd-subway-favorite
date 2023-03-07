package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FavoriteCreateRequest {

    @JsonProperty("source")
    private Long sourceId;

    @JsonProperty("target")
    private Long targetId;

    public FavoriteCreateRequest(final Long sourceId, final Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }
}
