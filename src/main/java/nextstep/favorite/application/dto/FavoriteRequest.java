package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteRequest {
    private Long source;
    private Long target;
}
