package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    private Member member;
    private Station sourceStation;
    private Station targetStation;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        member = new Member("test@example.com", "password", 30);
        sourceStation = new Station("신사역");
        targetStation = new Station("강남역");
        favorite = new Favorite(member, sourceStation, targetStation);
    }

    @Test
    @DisplayName("Favorite 객체가 올바르게 생성되는지 테스트")
    void createFavorite() {
        assertThat(favorite.getMember()).isEqualTo(member);
        assertThat(favorite.getSourceStation()).isEqualTo(sourceStation);
        assertThat(favorite.getTargetStation()).isEqualTo(targetStation);
    }

    @Test
    @DisplayName("Favorite 객체가 특정 멤버에 의해 소유되고 있는지 테스트")
    void isOwnedBy() {
        assertThat(favorite.isOwnedBy(member)).isTrue();

        Member anotherMember = new Member("another@example.com", "password", 25);
        assertThat(favorite.isOwnedBy(anotherMember)).isFalse();
    }
}