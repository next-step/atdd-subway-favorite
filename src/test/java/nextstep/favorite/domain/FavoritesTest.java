package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class FavoritesTest {

    private Station station1 = new Station();
    private Station station2 = new Station();
    private Member member = new Member();

    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {

        // given
        Favorite favorite = new Favorite(station1, station2, member);
        Favorites favorites = new Favorites();

        // when
        favorites.addFavorite(favorite);

        // then
        assertThat(favorites.getFavorites()).hasSize(1);
        assertThat(favorites.getFavorites()).containsExactly(favorite);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void removeFavorite() {

        // given
        long favoriteId = 1L;
        Favorite favorite = spy(Favorite.class);

        when(favorite.getId()).thenReturn(favoriteId);
        Favorites favorites = new Favorites();
        favorites.addFavorite(favorite);

        // when
        favorites.removeFavorite(favoriteId);

        // then
        assertThat(favorites.getFavorites()).hasSize(0);
    }

    @DisplayName("즐겨찾기 단건 조회")
    @Test
    void getFavorite() {
        // given
        Favorite favorite = spy(Favorite.class);

        long favoriteId = 1L;
        when(favorite.getId()).thenReturn(favoriteId);

        Favorites favorites = new Favorites();
        favorites.addFavorite(favorite);

        // when
        Favorite result = favorites.getFavorite(favoriteId);

        // then
        assertThat(result.getId()).isEqualTo(favoriteId);
    }
}