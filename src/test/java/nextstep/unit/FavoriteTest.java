package nextstep.unit;

import nextstep.auth.AuthenticationException;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoriteTest {
    Station 역삼역;
    Station 선릉역;
    Line 이호선;
    Member 유저_1번;
    Member 유저_2번;
    Favorite 유저_1번의_즐겨찾기;

    @BeforeEach()
    public void setup() {
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
        이호선 = new Line("이호선", "녹색");
        이호선.addSection(역삼역, 선릉역, 10);

        유저_1번 = new Member("email@email.com", "asdfasdf", 12);
        유저_2번 = new Member("email2@email.com", "asdfasdf", 12);
        유저_1번의_즐겨찾기 = new Favorite(유저_1번, 역삼역, 선릉역);
    }

    @Test
    @DisplayName("즐겨찾기의 유저가 동일하면 통과한다.")
    void checkOwnerSuccess() {
        // when, then
        유저_1번의_즐겨찾기.checkOwner(유저_1번);
    }

    @Test
    @DisplayName("즐겨찾기의 유저가 다르면 실패한다.")
    void checkOwnerFailure() {
        // when, then
        Assertions.assertThatThrownBy(() -> 유저_1번의_즐겨찾기.checkOwner(유저_2번)).isInstanceOf(AuthenticationException.class);
    }
}
