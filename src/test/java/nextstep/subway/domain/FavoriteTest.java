package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {

    @DisplayName("회원의 즐겨찾기인지 확인하는 테스트")
    @ParameterizedTest
    @CsvSource(value = {"1:true", "2:false"}, delimiter = ':')
    void isMembersFavoriteTest(Long memberId, boolean expected) {
        //given
        Long fixedMemberId = 1L;
        Favorite favorite = Favorite.of(fixedMemberId, null, null);

        //when
        boolean result = favorite.isMembersFavorite(memberId);

        //then
        assertThat(result).isEqualTo(expected);
    }

}