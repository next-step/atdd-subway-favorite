package nextstep.favorite.service.dto;

import lombok.Data;

@Data
public class FavoritePathRequest {
    private Long source;
    private Long target;
}
