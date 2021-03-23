package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteRequest {
    private String source;
    private String target;

    public FavoriteRequest() {}

    public FavoriteRequest(String source, String target){
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
