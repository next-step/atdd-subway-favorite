package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

@AllArgsConstructor
@Getter
public class FavoriteResponse {
    Long id;
    StationDTO source;
    StationDTO target;

    public FavoriteResponse(Favorite favorite) {
        this.source = new StationDTO(favorite.getSource());
        this.target = new StationDTO(favorite.getTarget());
    }

    @Getter
    @AllArgsConstructor
    public static class StationDTO {
        private Long id;
        private String name;

        public StationDTO(Station station){
            this.id = station.getId();
            this.name = station.getName();
        }
    }
}
