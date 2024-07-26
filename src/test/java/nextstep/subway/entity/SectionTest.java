package nextstep.subway.entity;

import nextstep.subway.exception.IllegalSectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.entity.EntityTestFixture.신사역;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구간 단위테스트")
class SectionTest {
    Line 신분당선;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "#77777");
    }

    @Test
    @DisplayName("구간의 상행역과 하행역이 같을경우 예외발생")
    void 구간의_상행역과_하행역이_같을경우_예외발생() {
        // when, then
        assertThatThrownBy(() -> new Section(신분당선, 신사역, 신사역, 10L))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("구간의 상행역과 하행역이 같을수없습니다.");
    }
}