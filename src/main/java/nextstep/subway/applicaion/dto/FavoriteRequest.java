package nextstep.subway.applicaion.dto;

public class FavoriteRequest {

    private Long source;
    private Long target;

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    private FavoriteRequest() {
    }

    private FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteRequest of(long source, long target) {
        return new FavoriteRequest(source, target);
    }

}
