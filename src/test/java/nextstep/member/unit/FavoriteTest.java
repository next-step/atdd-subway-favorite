package nextstep.member.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteStations;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;

class FavoriteTest {
    private final Station a = new Station("1");
    private final Station b = new Station("1");
    private final FavoriteStations stations = new FavoriteStations(a, b);
    private final Member memberA = new Member("test1@test.com", "1234", 1);
    private final Member memberB = new Member("test2@test.com", "1234", 1);
    private final Favorite favorite = new Favorite(memberA, stations);

    @Test
    void isCreatedMember() {
        boolean value = favorite.isCreatedMember(memberB);
        assertThat(value).isFalse();

        boolean value1 = favorite.isCreatedMember(memberA);
        assertThat(value1).isTrue();
    }
}
