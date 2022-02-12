package nextstep.favorite.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class FavoriteResponse {
    private Long id;
    private Long source;
    private Long target;

    private FavoriteResponse() {}

    @Builder
    private FavoriteResponse(Long id, Long source, Long target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return FavoriteResponse.builder()
            .id(favorite.getId())
            .source(favorite.getSourceId())
            .target(favorite.getTargetId())
            .build();
    }
}
