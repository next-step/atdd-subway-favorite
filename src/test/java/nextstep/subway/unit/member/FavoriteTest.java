package nextstep.subway.unit.member;

import nextstep.member.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.member.domain.exception.NotAuthorizedException;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("즐겨찾기 테스트")
class FavoriteTest {

    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Member 사용자;

    @BeforeEach
    void setUp() {
        사용자 = new Member("email@email.com");
    }

    @DisplayName("즐겨찾기를 등록할 수 있다.")
    @Test
    void createFavorite() {
        Favorite favorite = 사용자.addFavorite(강남역, 양재역);

        assertThat(사용자.getFavorites()).contains(favorite);
    }

    @DisplayName("즐겨찾기를 제거할 수 있다.")
    @Test
    void deleteFavorite() {
        Favorite favorite = 사용자.addFavorite(강남역, 양재역);

        사용자.deleteFavorite(favorite);

        assertThat(사용자.getFavorites()).isEmpty();
    }

    @DisplayName("다른 사용자의 즐겨찾기를 제거하는 경우 예외가 발생한다.")
    @Test
    void deleteFavoriteException() {
        Member 다른_사용자 = new Member("email@email.com");

        Favorite favorite = 다른_사용자.addFavorite(강남역, 양재역);

        assertThatThrownBy(() -> 사용자.deleteFavorite(favorite))
            .isInstanceOf(NotAuthorizedException.class);
    }
}
