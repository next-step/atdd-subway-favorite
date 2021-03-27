package nextstep.subway.favorite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import nextstep.subway.favorite.domain.Favorite;

public class FavoriteRequest {

    @JsonProperty("source")
    private Long sourceId;
    @JsonProperty("target")
    private Long targetId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Favorite toFavorite(Long memberId) {
        return new Favorite(memberId, sourceId, targetId);
    }
}
