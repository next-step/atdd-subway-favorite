package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;

public class FavoriteResponse {
    private final Long id;
    private final FavoriteStationDto source;
    private final FavoriteStationDto target;

    public FavoriteResponse(Long id, FavoriteStationDto source, FavoriteStationDto target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public FavoriteStationDto getSource() {
        return source;
    }

    public FavoriteStationDto getTarget() {
        return target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), FavoriteStationDto.from(favorite.getSource()), FavoriteStationDto.from(favorite.getTarget()));
    }
}
