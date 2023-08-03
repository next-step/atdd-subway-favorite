package nextstep.subway.applicaion.dto;

public class FavoriteRequest {

    private long source;
    private long target;

    public FavoriteRequest(){}

    public FavoriteRequest(long source, long target) {
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
