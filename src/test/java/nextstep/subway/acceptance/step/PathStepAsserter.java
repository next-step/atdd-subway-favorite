package nextstep.subway.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepAsserter {
    public static void 거리가_최소값인지_검증한다(Long actual, Long expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
