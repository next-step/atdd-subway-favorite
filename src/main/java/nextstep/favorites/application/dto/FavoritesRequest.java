package nextstep.favorites.application.dto;

import lombok.Getter;

@Getter
public class FavoritesRequest {
    private String source;
    private String target;

    public FavoritesRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }
}
