package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.domain.exception.FavoriteIsNotYoursException;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteTest {

    private Member memberA;
    private Member memberB;
    private Station stationA;
    private Station stationB;

    private Favorite favorite;

    // given: 즐겨찾기를 추가하고
    @BeforeEach
    void setUp() {
        memberA = new Member("a@test.com");
        ReflectionTestUtils.setField(memberA, "id", 1L);
        memberB = new Member("b@test.com");
        ReflectionTestUtils.setField(memberB, "id", 2L);

        stationA = new Station("stationA");
        stationB = new Station("stationB");

        favorite = new Favorite(stationA.getId(), stationB.getId(), memberA.getId());
    }

    @DisplayName("다른 사람의 즐겨찾기 항목인 경우 오류가 발생한다.")
    @Test
    void removeOtherMemberFavorite() {
        assertThatThrownBy(() -> favorite.validateRemove(memberB))
                .isInstanceOf(FavoriteIsNotYoursException.class);
    }
}
