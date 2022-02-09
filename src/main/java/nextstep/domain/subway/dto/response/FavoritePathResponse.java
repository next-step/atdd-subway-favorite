package nextstep.domain.subway.dto.response;


import nextstep.domain.subway.domain.FavoritePath;
import nextstep.domain.subway.domain.Station;
import nextstep.domain.subway.dto.StationResponse;

public class FavoritePathResponse {
    private Long id;
    private StationResponse startStation;
    private StationResponse endStation;

    public FavoritePathResponse() {
    }


    public FavoritePathResponse(Long id, StationResponse startStation, StationResponse endStation) {
        this.id = id;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public FavoritePathResponse(FavoritePath favoritePath) {
        this.id = favoritePath.getId();
        this.startStation = StationResponse.of(favoritePath.getStartStation());
        this.endStation = StationResponse.of(favoritePath.getEndStation());
    }

    public Long getId() {
        return id;
    }

    public StationResponse getStartStation() {
        return startStation;
    }

    public StationResponse getEndStation() {
        return endStation;
    }

}
