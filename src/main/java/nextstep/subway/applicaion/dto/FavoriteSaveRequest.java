package nextstep.subway.applicaion.dto;

public class FavoriteSaveRequest {
    private Long source;
    private Long target;

    public FavoriteSaveRequest() {
    }

    public FavoriteSaveRequest(final Long source, final Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
