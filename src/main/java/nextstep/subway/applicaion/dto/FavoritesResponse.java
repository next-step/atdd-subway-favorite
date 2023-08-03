package nextstep.subway.applicaion.dto;


import nextstep.subway.domain.Favorite;

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


    public static FavoritesResponse of(Favorite favorite) {
        return new FavoritesResponse(
            favorite.getId(),
            StationResponse.of(favorite.getSource()),
            StationResponse.of(favorite.getTarget())
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
