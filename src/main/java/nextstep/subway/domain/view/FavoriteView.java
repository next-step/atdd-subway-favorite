package nextstep.subway.domain.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nextstep.subway.domain.view.StationView;

public class FavoriteView {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Main {
        private Long id;
        private StationView.Main source;
        private StationView.Main target;
    }
}
