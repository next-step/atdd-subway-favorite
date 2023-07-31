package nextstep.favorite.ui;

public class FavoriteCreateRequest {

    private long source;
    private long target;

    public FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(long source, long target) {
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
