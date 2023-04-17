package nextstep.subway.unit;

import nextstep.exception.SubwayIllegalArgumentException;
import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 테스트")
class FavoriteTest {
    private Member member;
    private Station 강남역;
    private Station 판교역;
    @BeforeEach
    void setUp() {
        member = new Member("test@test.com", "password", 33);
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
    }

    @Test
    @DisplayName("즐겨찾기 생성에 성공한다")
    void 즐겨찾기_생성에_성공한다() {

        // when
        Favorite favorite = new Favorite(member, 강남역, 판교역);

        // then
        assertAll(
                () -> assertThat(favorite.getMember()).isEqualTo(member),
                () -> assertThat(favorite.getSource()).isEqualTo(강남역),
                () -> assertThat(favorite.getTarget()).isEqualTo(판교역)
        );
    }

    @Test
    @DisplayName("즐겨찾기 생성시 출발역과 도착역이 같을경우 예외가 발생한다")
    void 즐겨찾기_생성시_출발역과_도착역이_같을경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Favorite(member, 강남역, 강남역))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 같을 수 없습니다.");
    }

    @NullSource
    @ParameterizedTest
    @DisplayName("즐겨찾기 생성시 출발역을 지정하지 않으면 예외가 발생한다")
    void 즐겨찾기_생성시_출발역을_지정하지_않으면_예외가_발생한다(Station source) {
        assertThatThrownBy(() -> new Favorite(member, source, 판교역))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("출발역과, 도착역 둘다 입력해줘야 합니다.");
    }

    @NullSource
    @ParameterizedTest
    @DisplayName("즐겨찾기 생성시 도착역을 지정하지 않으면 예외가 발생한다")
    void 즐겨찾기_생성시_도착역을_지정하지_않으면_예외가_발생한다(Station target) {
        assertThatThrownBy(() -> new Favorite(member, 강남역, target))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("출발역과, 도착역 둘다 입력해줘야 합니다.");
    }
}
