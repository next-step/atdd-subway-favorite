package nextstep.favorite.application.response;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.application.dto.ShowStationDto;

public class AddFavoriteResponse {

    private Long favoriteId;
    private ShowStationDto startStation;
    private ShowStationDto endStation;

    private AddFavoriteResponse() {
    }

    private AddFavoriteResponse(Long favoriteId, ShowStationDto startStation, ShowStationDto endStation) {
        this.favoriteId = favoriteId;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public static AddFavoriteResponse from(Favorite favorite) {
        return new AddFavoriteResponse(
                favorite.getFavoriteId(),
                ShowStationDto.from(favorite.getStartStation()),
                ShowStationDto.from(favorite.getEndStation())
        );
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
