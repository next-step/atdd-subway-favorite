package nextstep.subway.domain.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nextstep.subway.domain.entity.station.Station;

public class StationView {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Main {
        private Long id;
        private String name;

        public static Main of(Station station) {
            return new Main(station.getId(), station.getName());
        }
    }
}
