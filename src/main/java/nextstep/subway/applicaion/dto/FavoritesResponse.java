package nextstep.subway.applicaion.dto;


import nextstep.subway.domain.Favorites;

public class FavoritesResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoritesResponse() {}

    public FavoritesResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }


    public static FavoritesResponse of(Favorites favorites) {
        return new FavoritesResponse(
            favorites.getId(),
            StationResponse.of(favorites.getSource()),
            StationResponse.of(favorites.getTarget())
        );
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
