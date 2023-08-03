package nextstep.favorite.application.dto;

public class FavoriteSaveRequest {

    private Long source;
    private Long target;

    public FavoriteSaveRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public FavoriteSaveRequest() {
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
