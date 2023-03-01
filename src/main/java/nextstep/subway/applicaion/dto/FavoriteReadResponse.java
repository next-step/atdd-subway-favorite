package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

@Getter
public class FavoriteReadResponse {
    private Long id;
    private FavoriteReadStationResponse source;
    private FavoriteReadStationResponse target;

    public FavoriteReadResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = new FavoriteReadStationResponse(favorite.getSource());
        this.target = new FavoriteReadStationResponse(favorite.getTarget());
    }

    @Getter
    public class FavoriteReadStationResponse {
        private Long id;
        private String name;

        public FavoriteReadStationResponse(Station station) {
            this.id = station.getId();
            this.name = station.getName();
        }
    }
}
