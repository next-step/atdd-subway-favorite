package nextstep.favorites.application.dto;

public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }


    public FavoriteRequest() {}

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
