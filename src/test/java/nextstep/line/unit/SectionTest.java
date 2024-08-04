package nextstep.line.unit;

import nextstep.line.domain.Section;
import nextstep.line.application.exception.NotLessThanExistingDistanceException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구간 도메인 테스트")
public class SectionTest {

    @Test
    @DisplayName("구간 분리 함수는, 현재 구간의 상행역과 주어진 구간의 하행역이 연결된다.")
    void dividedSectionTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section 강남역_홍대역 = createSection(신분당선, 강남역, 홍대역, DISTANCE_4);
        Section 홍대역_양재역 = createSection(신분당선, 홍대역, 양재역, DISTANCE_6);

        // when
        Section actual = 강남역_양재역.dividedSection(강남역_홍대역);

        // then
        assertThat(actual).isEqualTo(홍대역_양재역);
    }

    @Test
    @DisplayName("구간 분리 함시 실행 중, 새로운 구간의 거리가 기존 구간의 거리보다 크거나 같으면 예외를 발생시킨다.")
    void GraterOrEqualExistingDistanceExceptionTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section 강남역_홍대역 = createSection(신분당선, 강남역, 홍대역, DEFAULT_DISTANCE);

        // when
        ThrowingCallable actual = () -> 강남역_양재역.dividedSection(강남역_홍대역);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotLessThanExistingDistanceException.class);
    }

    @Test
    @DisplayName("상행역 비교 함수는, 현재 구간과 주어진 구간의 상행역이 같은지 확인한다.")
    void sameUpStationTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section 강남역_홍대역 = createSection(신분당선, 강남역, 홍대역, DEFAULT_DISTANCE);

        // when & then
        assertThat(강남역_양재역.sameUpStation(강남역_홍대역)).isTrue();
    }

    @Test
    @DisplayName("하행역과 상행역 비교 함수는, 현재 구간의 하행역과 주어진 구간의 상행역이 같은지 확인한다.")
    void sameDownStationAndUpStationOfNewSectionTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section 양재역_교대역 = createSection(신분당선, 양재역, 교대역, DEFAULT_DISTANCE);

        // when & then
        assertThat(강남역_양재역.sameDownStationAndUpStationOf(양재역_교대역)).isTrue();
    }

    @Test
    @DisplayName("상행역과 상행역 비교 함수는, 현재 구간의 상행역과 주어진 구간의 하행역이 같은지 확인한다.")
    void sameUpStationAndDownStationOfNewSectionTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section 양재역_교대역 = createSection(신분당선, 양재역, 교대역, DEFAULT_DISTANCE);

        // when & then
        assertThat(양재역_교대역.sameUpStationAndDownStationOf(강남역_양재역)).isTrue();
    }

    @Test
    @DisplayName("첫 번째로 변경 함수는, 구간의 첫 번째를 판단하는 상태 값을 true로 바꾼다.")
    void changeToFirstTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);

        // when
        강남역_양재역.changeToFirst();

        // then
        assertThat(강남역_양재역.isFirst()).isTrue();
    }

    @Test
    @DisplayName("첫 번째가 아닌걸로 변경하는 함수는, 구간의 첫 번째를 판단하는 상태 값을 false로 바꾼다.")
    void changeToNotFirstTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);

        // when
        강남역_양재역.changeToNotFirst();

        // then
        assertThat(강남역_양재역.isFirst()).isFalse();
    }
}
