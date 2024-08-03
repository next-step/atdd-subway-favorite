package nextstep.favorite.application.dto;

import javax.validation.constraints.NotNull;

public class FavoriteRequest {
    @NotNull
    private Long source;
    @NotNull
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteRequest of (Long source, Long target) {
        return new FavoriteRequest(source, target);
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

}
