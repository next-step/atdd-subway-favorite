package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;

public class FavoriteRequest {

    private Long source;
    private Long target;

    public FavoriteRequest() {

    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

}
