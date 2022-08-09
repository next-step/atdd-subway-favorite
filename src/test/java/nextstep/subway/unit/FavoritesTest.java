package nextstep.subway.unit;

import nextstep.member.domain.Favorite;
import nextstep.member.domain.Favorites;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoritesTest {

    @Test
    void 즐겨찾기_등록() {
        //given
        Member user = new Member("parkuram12@gmail.com", "pass", 25);
        Favorite favorite = new Favorite(user, 1L, 2L);

        //when
        Favorites favorites = new Favorites(new ArrayList<>());
        favorites.addFavorite(favorite);

        //then
        assertThat(favorites.getValue()).hasSize(1);
    }

    @Test
    void 중복된_즐겨찾기_추가() {
        //given
        Member user = new Member("parkuram12@gmail.com", "pass", 25);
        Favorite favorite = new Favorite(user, 1L, 2L);

        //when
        Favorites favorites = new Favorites(new ArrayList<>());
        favorites.addFavorite(favorite);

        //then
        assertThatThrownBy( () -> favorites.addFavorite(favorite))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 등록된 즐겨찾기 입니다.");
    }

}
