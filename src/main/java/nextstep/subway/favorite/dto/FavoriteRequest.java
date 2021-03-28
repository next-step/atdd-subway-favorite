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

    public boolean isValid() {
        if (source == null || target == null) {
            return false;
        }

        if (source < 0 || target < 0) {
            return false;
        }

        return true;
    }
}
