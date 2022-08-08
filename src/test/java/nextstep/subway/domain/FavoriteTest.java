package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.fixture.MemberFixture.MEMBER_A;
import static nextstep.subway.domain.fixture.MemberFixture.MEMBER_B;
import static nextstep.subway.domain.fixture.StationFixture.GANGNAM;
import static nextstep.subway.domain.fixture.StationFixture.YEOKSAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class FavoriteTest {

    @Test
    @DisplayName("출발역과 도착역이 동일하면 즐겨찾기를 추가할 수 없다.")
    void create_invalid() {
        // when
        assertThatThrownBy(() -> Favorite.create(MEMBER_A.getId(), GANGNAM, GANGNAM))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("즐겨찾기 소유자 여부를 판단한다.")
    void isNotOwner() {
        // when
        Favorite favorite = Favorite.create(MEMBER_A.getId(), GANGNAM, YEOKSAM);

        // then
        assertAll(() -> {
            assertThat(favorite.isNotOwner(MEMBER_A)).isFalse();
            assertThat(favorite.isNotOwner(MEMBER_B)).isTrue();
        });
    }
}