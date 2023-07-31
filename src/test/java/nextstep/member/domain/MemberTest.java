package nextstep.member.domain;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class MemberTest {

    private Station station1 = new Station();
    private Station station2 = new Station();
    private Member member = new Member();

    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {

        // given
        Favorite favorite = new Favorite(station1, station2, member);

        // when
        member.addFavorite(favorite);

        // then
        assertThat(member.getFavorites()).hasSize(1);
        assertThat(member.getFavorites()).containsExactly(favorite);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void removeFavorite() {

        // given
        Favorite favorite = new Favorite(station1, station2, member);
        member.addFavorite(favorite);

        // when
        member.removeFavorite(favorite);

        // then
        assertThat(member.getFavorites()).hasSize(0);
    }

    @DisplayName("즐겨찾기 단건 조회")
    @Test
    void getFavorite() {
        // given
        Favorite favorite = spy(Favorite.class);

        long favoriteId = 1L;
        when(favorite.getId()).thenReturn(favoriteId);

        member.addFavorite(favorite);

        // when
        Favorite result = member.getFavorite(favoriteId);

        // then
        assertThat(result.getId()).isEqualTo(favoriteId);
    }

}