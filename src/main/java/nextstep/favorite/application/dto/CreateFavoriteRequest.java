package nextstep.favorite.application.dto;

public class CreateFavoriteRequest {
    private Long target;
    private Long source;

    public CreateFavoriteRequest(Long target, Long source) {
        this.target = target;
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public Long getSource() {
        return source;
    }
}
