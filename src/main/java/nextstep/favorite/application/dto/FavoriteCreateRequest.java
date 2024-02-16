package nextstep.favorite.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCreateRequest {
    @JsonProperty("source")
    private Long sourceId;
    @JsonProperty("target")
    private Long targetId;

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
