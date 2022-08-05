package nextstep.subway.applicaion.dto;

public class FavoriteResponse {

    private final long id;
    private final long source;
    private final long target;

    public FavoriteResponse(final long id, final long source, final long target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public long getId() {
        return id;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }

}
