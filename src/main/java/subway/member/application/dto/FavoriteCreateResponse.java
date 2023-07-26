package subway.member.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.member.domain.Favorite;

@Getter
@Builder
public class FavoriteCreateResponse {
    private Long id;

    public static FavoriteCreateResponse from(Favorite favorite) {
        return FavoriteCreateResponse.builder()
                .id(favorite.getId())
                .build();
    }
}
