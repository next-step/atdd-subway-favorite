package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public boolean isValidRequest() {
        if (this.source == null || source < 0) {
            return false;
        }
        if (this.target == null || target < 0) {
            return false;
        }
        if (this.target == source) {
            return false;
        }
        return true;
    }
}
