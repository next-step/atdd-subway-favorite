package nextstep.favorite.unit;

import static nextstep.subway.fixture.StationFixture.강남역_이름;
import static nextstep.subway.fixture.StationFixture.교대역_이름;
import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.member.fixture.MemberFixture;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.fixture.StationFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾ㅈ기 단위테스트")
class FavoriteTest {

    private Member 사용자A;
    private Member 사용자B;
    private Station 강남역;
    private Station 교대역;


    @BeforeEach
    void setUp() {
        강남역 = StationFixture.giveOne(1L, 강남역_이름);
        교대역 = StationFixture.giveOne(2L, 교대역_이름);
        사용자A = MemberFixture.giveOne(1L, "email", "password", 10);
        사용자B = MemberFixture.giveOne(2L, "email_2", "password", 10);


    }


    @DisplayName("즐겨찾기 소유 권한 확인 - 소유자인 경우")
    @Test
    void isOwnerTrue() {
        // given
        Favorite favorite = Favorite.of(사용자A.getId(), 강남역, 교대역);

        // when
        var isOwner = favorite.isOwner(사용자A);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(isOwner).isTrue();
        });
    }

    @DisplayName("즐겨찾기 소유 권한 확인 - 소유자가 아닌 경우")
    @Test
    void isOwnerFalse() {
        // given
        Favorite favorite = Favorite.of(사용자A.getId(), 강남역, 교대역);

        // when
        var isOwner = favorite.isOwner(사용자B);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(isOwner).isFalse();
        });
    }

}
