package nextstep.favorite.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

public class FavoriteResponseDto {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    protected FavoriteResponseDto() {
    }

    public FavoriteResponseDto(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponseDto of(Favorite favorite) {
        return new FavoriteResponseDto(
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
