package nextstep.favorite.application.dto;

import lombok.Getter;

@Getter
public class FavoriteRequest {
    private long source;
    private long target;

    private FavoriteRequest() {}

    public FavoriteRequest(long source, long target) {
        this.source = source;
        this.target = target;
    }
}
