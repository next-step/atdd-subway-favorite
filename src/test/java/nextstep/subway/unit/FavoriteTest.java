package nextstep.subway.unit;

import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteTest {

    @DisplayName("즐겨찾기를 정상적으로 생성한다")
    @Test
    public void create_favorite_test() {
        // given
        Member member = new Member(1L, "email", "password", 20);
        Station 출발역 = new Station(2L, "출발역");
        Station 도착역 = new Station(3L, "도착역");

        // when
        Favorite favorite = new Favorite(member.getId(), 출발역, 도착역);

        // then
        assertAll(
                () -> assertThat(favorite.getMemberId()).isEqualTo(member.getId()),
                () -> assertThat(favorite.getSource()).isEqualTo(출발역),
                () -> assertThat(favorite.getTarget()).isEqualTo(도착역)
        );
    }
}
