package nextstep.subway.applicaion.dto;

public class FavoriteRequest {

    private final String source;
    private final String target;

    public FavoriteRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

}
