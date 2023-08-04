package nextstep.favorite.application.dto;

public class CreateFavoriteRequest {
    private Long source;
    private Long target;

    public CreateFavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getTarget() {
        return target;
    }

    public Long getSource() {
        return source;
    }
}
