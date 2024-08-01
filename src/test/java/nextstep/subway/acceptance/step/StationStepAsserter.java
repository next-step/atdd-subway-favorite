package nextstep.subway.acceptance.step;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StationStepAsserter {
    public static void 역_목록에_지정된_역이_포함되는지_검증한다(List<String> actual, String expected) {
        assertThat(actual).containsAnyOf(expected);
    }

    public static void 역_목록에_지정된_역이_포함되지_않는지_검증한다(List<String> actual, String expected) {
        assertThat(actual).doesNotContain(expected);
    }

    public static void 역_목록에_지정된_역들이_순서대로_포함되는지_검증한다(List<String> actual, String... expected) {
        assertThat(actual).containsExactly(expected);
    }

    public static void 역_목록에_지정된_역들이_포함되는지_검증한다(List<String> actual, String... expected) {
        assertThat(actual).containsExactlyInAnyOrder(expected);
    }

    public static void 역_목록의_크기를_검증한다(List<String> stationNames, int size) {
        assertThat(stationNames.size()).isEqualTo(size);
    }
}
