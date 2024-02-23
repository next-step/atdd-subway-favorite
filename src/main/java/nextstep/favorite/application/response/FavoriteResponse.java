package nextstep.favorite.application.response;

import nextstep.subway.application.dto.ShowStationDto;

public class FavoriteResponse {

    private Long favoriteId;
    private ShowStationDto startStation;
    private ShowStationDto endStation;

    private FavoriteResponse() {
    }

    private FavoriteResponse(Long favoriteId, ShowStationDto startStation, ShowStationDto endStation) {
        this.favoriteId = favoriteId;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public static FavoriteResponse of(Long favoriteId, ShowStationDto startStation, ShowStationDto endStation) {
        return new FavoriteResponse(favoriteId, startStation, endStation);
    }

    public Long getFavoriteId() {
        return favoriteId;
    }

    public ShowStationDto getStartStation() {
        return startStation;
    }

    public ShowStationDto getEndStation() {
        return endStation;
    }

}
