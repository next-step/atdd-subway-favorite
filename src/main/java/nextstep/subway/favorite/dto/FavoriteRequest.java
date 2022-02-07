package nextstep.subway.favorite.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteRequest {
    Long source;
    Long target;

    private FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteRequest of(Long source, Long target) {
        return new FavoriteRequest(source, target);
    }
}
