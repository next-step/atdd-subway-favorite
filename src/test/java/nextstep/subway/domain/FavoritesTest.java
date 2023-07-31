package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.mock;

import nextstep.auth.AuthenticationException;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoritesTest {

    @Test
    @DisplayName("즐겨찾기 삭제시 소유하는 유저가 아니라면 exception")
    void validDelete() {

        //given
        Station source = mock(Station.class);
        Station target = mock(Station.class);
        Member member = mock(Member.class);

        Favorites favorites = Favorites.of(source, target, member);

        //then
        assertThatThrownBy(
            () -> favorites.validDelete(new Member())
        ).isInstanceOf(AuthenticationException.class);
    }
}