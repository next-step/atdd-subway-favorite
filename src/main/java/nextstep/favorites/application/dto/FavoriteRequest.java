package nextstep.favorites.application.dto;

import lombok.Getter;

@Getter
public class FavoriteRequest {
    private String source;
    private String target;

    public FavoriteRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }
}
