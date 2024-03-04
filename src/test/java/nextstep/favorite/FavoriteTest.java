package nextstep.favorite;

import nextstep.favorite.domain.Favorite;
import nextstep.auth.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.station.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {

    private Station 교대역;
    private Station 강남역;
    private Member me;
    private Member other;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        me = new Member("me@mail.com", "me", 10);
        other = new Member("other@mail.com", "other", 10);
    }

    @DisplayName("내가 등록한 즐겨찾기가 아니면 에러를 반환한다.")
    @Test
    void validateDeletionByMember() {
        Favorite favorite = new Favorite(교대역, 강남역, other);

        assertThatThrownBy(() -> favorite.validateDeletionByMember(me))
                .isInstanceOf(AuthenticationException.class);
    }
}
