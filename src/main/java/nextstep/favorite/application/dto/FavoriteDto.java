package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.application.dto.StationDto;

public class FavoriteDto {

    private Long favoriteId;
    private StationDto startStation;
    private StationDto endStation;

    private FavoriteDto() {
    }

    private FavoriteDto(Long favoriteId, StationDto startStation, StationDto endStation) {
        this.favoriteId = favoriteId;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public static FavoriteDto from(Favorite favorite) {
        return new FavoriteDto(
                favorite.getFavoriteId(),
                StationDto.from(favorite.getStartStation()),
                StationDto.from(favorite.getEndStation())
        );
    }

    public Long getFavoriteId() {
        return favoriteId;
    }

    public StationDto getStartStation() {
        return startStation;
    }

    public StationDto getEndStation() {
        return endStation;
    }

}
