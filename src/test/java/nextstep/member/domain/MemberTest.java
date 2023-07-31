package nextstep.member.domain;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Assertions.assertThat(member.getFavorites()).hasSize(1);
        Assertions.assertThat(member.getFavorites()).containsExactly(favorite);
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
        Assertions.assertThat(member.getFavorites()).hasSize(0);
    }

}