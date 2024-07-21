package nextstep.subway.acceptance.step;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStepAsserter {
    public static void 노선_이름이_일치하는지_검증한다(String actual, String expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void 노선_색상이_일치하는지_검증한다(String actual, String expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void 노선_목록에_지정된_노선이_포함되는지_검증한다(List<String> actual, String expected) {
        assertThat(actual).containsAnyOf(expected);
    }

    public static void 노선_목록에_지정된_노선이_포함되지_않는지_검증한다(List<String> actual, String expected) {
        assertThat(actual).doesNotContain(expected);
    }

    public static void 노선_목록에_지정된_노선들이_포함되는지_검증한다(List<String> actual, String... expected) {
        assertThat(actual).containsExactly(expected);
    }
}
