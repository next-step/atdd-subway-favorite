package nextstep.subway.domain.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PathView {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Main {
        private List<StationView.Main> stations;
        private Long distance;
    }
}
