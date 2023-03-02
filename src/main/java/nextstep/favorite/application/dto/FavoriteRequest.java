package nextstep.favorite.application.dto;

public class FavoriteRequest {

    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(final Long source, final Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
