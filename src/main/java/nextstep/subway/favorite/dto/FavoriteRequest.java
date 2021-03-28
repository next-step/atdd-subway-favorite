package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private long source;

    private long target;

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }
}
