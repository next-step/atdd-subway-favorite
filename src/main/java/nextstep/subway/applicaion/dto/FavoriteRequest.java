package nextstep.subway.applicaion.dto;

public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest(Long source, Long target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException();
        }

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
