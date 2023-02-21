package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {

    @Test
    void 즐겨찾기를_생성한다() {
        //given
        Member 사용자 = new Member("email@email.com", "password", 10);
        Station 강남역 = new Station("강남역");
        Station 정자역 = new Station("정자역");

        //when
        Favorite favorite = new Favorite(사용자, 강남역, 정자역);

        //then
        assertThat(favorite).isNotNull();
    }
}
