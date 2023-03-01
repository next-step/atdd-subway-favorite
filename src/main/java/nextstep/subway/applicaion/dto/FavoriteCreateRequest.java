package nextstep.subway.applicaion.dto;

public class FavoriteCreateRequest {

    private Long source;
    private Long target;

    public FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(Long source, Long target) {
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
