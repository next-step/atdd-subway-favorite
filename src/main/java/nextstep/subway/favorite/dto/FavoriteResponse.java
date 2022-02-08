package nextstep.subway.favorite.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.dto.StationResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Long id, StationResponse source, StationResponse target) {
       return new FavoriteResponse(id, source, target);
    }
}
