package nextstep.subway.application.dto;

public class FavouriteRequest {
    private Long source;
    private Long target;

    public FavouriteRequest() {

    }

    public FavouriteRequest(Long source, Long target) {
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
