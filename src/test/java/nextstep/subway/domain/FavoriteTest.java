package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.fixture.MemberFixture.MEMBER;
import static nextstep.subway.domain.fixture.StationFixture.GANGNAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {

    @Test
    @DisplayName("출발역과 도착역이 동일하면 즐겨찾기를 추가할 수 없다.")
    void create_invalid() {
        // when
        assertThatThrownBy(() -> Favorite.create(MEMBER.getId(), GANGNAM, GANGNAM))
                .isInstanceOf(IllegalArgumentException.class);
    }
}