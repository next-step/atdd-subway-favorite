package nextstep.member.domain;

import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 10;

    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        // given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Station 곰역 = new Station("곰역");
        Station 다람쥐역 = new Station("다람쥐역");
        Favorite favorite = new Favorite(member, 곰역, 다람쥐역);

        // when
        member.addFavorite(favorite);

        // then
        assertThat(member.getFavorites()).containsExactly(favorite);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void removeFavorite() {
        // given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Station 곰역 = new Station("곰역");
        Station 다람쥐역 = new Station("다람쥐역");
        Favorite favorite = new Favorite(member, 곰역, 다람쥐역);

        // when
        member.removeFavorite(favorite);

        // then
        assertThat(member.getFavorites()).doesNotContain(favorite);
    }
}