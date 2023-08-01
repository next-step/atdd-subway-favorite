package nextstep.favorite.dto;

public class FavoriteCreateRequest {
    private Long source;
    private Long target;

    private FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(Long source, Long target) {
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
