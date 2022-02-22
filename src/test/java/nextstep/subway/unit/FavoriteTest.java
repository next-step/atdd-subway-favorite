package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FavoriteTest {

    @Test
    void validateMember() {
        // given
        final Station source = new Station("강남역");
        final Station target = new Station("역삼역");
        final Long memberId = 1L;
        final Long otherMemberId = 2L;
        final Favorite favorite = new Favorite(memberId, source, target);

        // when
        // then
        assertThatExceptionOfType(AuthenticationException.class).isThrownBy(() -> favorite.validateMember(otherMemberId));
    }
}
