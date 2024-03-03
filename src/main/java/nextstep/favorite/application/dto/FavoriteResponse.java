package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.domain.entity.Station;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */

@AllArgsConstructor
@Getter
public class FavoriteResponse {

    @Getter
    public static class StationDto {

        private final Long id;
        private final String name;

        public StationDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public StationDto(Station station) {
            this.id = station.getId();
            this.name = station.getName();
        }
    }

    private Long id;
    private StationDto source;
    private StationDto target;

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = new StationDto(favorite.getSource());
        this.target = new StationDto(favorite.getTarget());

    }


}
