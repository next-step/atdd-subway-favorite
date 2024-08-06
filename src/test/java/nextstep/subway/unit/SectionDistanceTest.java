package nextstep.subway.unit;

import nextstep.subway.domain.SectionDistance;
import nextstep.common.InvalidSectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionDistanceTest {

    @Test
    @DisplayName("유효한 구간 거리 생성")
    void createSectionDistanceWithValidDistance() {
        // when
        SectionDistance distance = new SectionDistance(5);

        // then
        assertThat(distance.getDistance()).isEqualTo(5);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    @DisplayName("0 이하의 구간 거리 생성 시 예외 발생")
    void createSectionDistanceWithInvalidDistance(int invalidDistance) {
        // when & then
        assertThatThrownBy(() -> new SectionDistance(invalidDistance))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("구간 거리는 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("구간 거리 뺄셈 연산")
    void subtractSectionDistances() {
        // given
        SectionDistance distance1 = new SectionDistance(10);
        SectionDistance distance2 = new SectionDistance(3);

        // when
        SectionDistance result = distance1.minus(distance2);

        // then
        assertThat(result.getDistance()).isEqualTo(7);
    }

    @Test
    @DisplayName("구간 거리 덧셈 연산")
    void addSectionDistances() {
        // given
        SectionDistance distance1 = new SectionDistance(5);
        SectionDistance distance2 = new SectionDistance(3);

        // when
        SectionDistance result = distance1.plus(distance2);

        // then
        assertThat(result.getDistance()).isEqualTo(8);
    }

    @Test
    @DisplayName("구간 거리 비교 (작거나 같음)")
    void compareSectionDistances() {
        // given
        SectionDistance distance1 = new SectionDistance(5);
        SectionDistance distance2 = new SectionDistance(7);
        SectionDistance distance3 = new SectionDistance(5);

        // when & then
        assertThat(distance1.isLessThanOrEqualTo(distance2)).isTrue();
        assertThat(distance1.isLessThanOrEqualTo(distance3)).isTrue();
        assertThat(distance2.isLessThanOrEqualTo(distance1)).isFalse();
    }
}
