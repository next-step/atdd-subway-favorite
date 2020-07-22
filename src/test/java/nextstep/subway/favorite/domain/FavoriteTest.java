package nextstep.subway.favorite.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 도메인 테스트")
class FavoriteTest {

    @Test
    @DisplayName("즐겨찾기는 회원 아이디, 출발역, 도착역에 대한 정보를 가지고 있다")
    void getInfo() {
        //given
        long sourceStationId = 1L;
        long targetStationId = 2L;
        long memberId = 1L;
        Favorite favorite = new Favorite(sourceStationId, targetStationId, memberId);

        //when
        assertThat(favorite).isNotNull()
                .hasFieldOrPropertyWithValue("sourceStationId", sourceStationId)
                .hasFieldOrPropertyWithValue("targetStationId", targetStationId)
                .hasFieldOrPropertyWithValue("memberId", memberId);
    }
}