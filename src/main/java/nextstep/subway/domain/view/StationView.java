package nextstep.subway.domain.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class StationView {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Main {
        private Long id;
        private String name;
    }
}
