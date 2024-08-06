package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.line.domain.entity.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    @DisplayName("유효한 거리로 Distance 객체를 생성한다.")
    void createValidDistance() {
        // given
        Long validDistance = 10L;

        // when
        Distance distance = new Distance(validDistance);

        // then
        assertThat(distance.getDistance()).isEqualTo(validDistance);
    }

    @Test
    @DisplayName("유효하지 않은 거리로 Distance 객체 생성 시 예외가 발생한다.")
    void createInvalidDistance() {
        // given
        Long invalidDistance = -10L;

        // when, then
        assertThrows(SubwayException.class, () -> {
            new Distance(invalidDistance);
        });
    }

    @Test
    @DisplayName("유효한 거리로 Distance 객체를 업데이트한다.")
    void updateValidDistance() {
        // given
        Distance distance = new Distance(10L);
        Long newValidDistance = 20L;

        // when
        distance.updateDistance(newValidDistance);

        // then
        assertThat(distance.getDistance()).isEqualTo(newValidDistance);
    }

    @Test
    @DisplayName("유효하지 않은 거리로 Distance 객체를 업데이트 시 예외가 발생한다.")
    void updateInvalidDistance() {
        // given
        Distance distance = new Distance(10L);
        Long newInvalidDistance = -20L;

        // when, then
        assertThrows(SubwayException.class, () -> {
            distance.updateDistance(newInvalidDistance);
        });
    }
}
