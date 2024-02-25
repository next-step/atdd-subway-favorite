package nextstep.favorite.domain;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.ui.BusinessException;
import nextstep.utils.FixtureUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateFavoriteValidatorTest {

  @DisplayName("즐겨찾기 생성 검증 성공")
  @Test
  void validate() {
    // given
    final var 강남역 = FixtureUtil.getFixture(Station.class);
    final var 역삼역 = FixtureUtil.getFixture(Station.class);
    final var sections = List.of(구간_생성(강남역, 역삼역, 10));

    // when
    final var result = catchThrowable(() -> CreateFavoriteValidator.validate(강남역, 역삼역, sections));

    // then
    assertThat(result).isNull();
  }

  @DisplayName("즐겨찾기 생성 검증 실패 - 출발역과 도착역이 같음")
  @Test
  void 즐겨찾기_생성_검증_실패_출발역과_도착역이_같음() {
    // given
    final var 강남역 = FixtureUtil.getFixture(Station.class);
    final var 역삼역 = FixtureUtil.getFixture(Station.class);
    final var sections = List.of(구간_생성(강남역, 역삼역, 5));

    // when
    final var result = catchThrowable(() -> CreateFavoriteValidator.validate(강남역, 강남역, sections));

    // then
    assertThat(result).isInstanceOf(BusinessException.class)
        .hasMessageContaining("출발역과 도착역을 다르게 설정해주세요.");
  }

  @DisplayName("즐겨찾기 생성 검증 실패 - 연결되지 않는 경로")
  @Test
  void 즐겨찾기_생성_검증_실패_연결되지_않는_경로() {
    // given
    final var 강남역 = FixtureUtil.getFixture(Station.class);
    final var 역삼역 = FixtureUtil.getFixture(Station.class);
    final var 서면역 = FixtureUtil.getFixture(Station.class);
    final var 남포역 = FixtureUtil.getFixture(Station.class);
    final var 구간목록 = List.of(
        구간_생성(강남역, 역삼역, 5),
        구간_생성(서면역, 남포역, 10)
    );

    // when
    final var result = catchThrowable(() -> CreateFavoriteValidator.validate(강남역, 남포역, 구간목록));

    // then
    assertThat(result).isInstanceOf(BusinessException.class)
        .hasMessageContaining("이어지지 않는 경로입니다.");
  }

  private Section 구간_생성(Station source, Station target, int distance) {
    return FixtureUtil.getBuilder(Section.class)
        .set(javaGetter(Section::getUpStation), source)
        .set(javaGetter(Section::getDownStation), target)
        .set(javaGetter(Section::getDistance), distance)
        .setNull(javaGetter(Section::getLine))
        .sample();
  }
}