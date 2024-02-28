package nextstep.favorite.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteCreateResponse {
    private Long id;
    
    @Builder
    public FavoriteCreateResponse(Long id) {
        this.id = id;
    }
}
