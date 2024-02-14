package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCreateRequest {
    private Long source;
    private Long target;
}
