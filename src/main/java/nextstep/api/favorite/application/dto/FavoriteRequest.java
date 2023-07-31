package nextstep.api.favorite.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FavoriteRequest {
    @JsonProperty("source")
    private Long sourceId;
    @JsonProperty("target")
    private Long targetId;

    public FavoriteRequest(final Long source, final Long target) {
        this.sourceId = source;
        this.targetId = target;
    }
}
