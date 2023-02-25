package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.domain.exception.FavoriteIsNotYoursException;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {

    private Member memberA;
    private Member memberB;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        memberA = new Member("a@test.com");
        memberB = new Member("b@test.com");

        favorite = new Favorite(new Station("stationA"), new Station("stationB"), memberA);
    }

    @DisplayName("다른 사람의 즐겨찾기를 삭제하면 오류가 발생한다.")
    @Test
    void removeOtherMemberFavorite() {
        assertThatThrownBy(() -> memberB.removeFavorite(favorite))
                .isInstanceOf(FavoriteIsNotYoursException.class);
    }
}
