package nextstep.subway.domain.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class LineView {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Main {
        private Long id;
        private String name;
        private String color;
        private List<StationView.Main> stations;
    }
}
