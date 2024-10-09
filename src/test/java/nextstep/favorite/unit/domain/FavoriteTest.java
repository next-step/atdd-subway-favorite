package nextstep.favorite.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.favorite.domain.Favorite;
import org.junit.jupiter.api.Test;

public class FavoriteTest {

    @Test
    void 즐겨찾기를_생성한다() {
        // given
        Long memberId = 1L;
        Long sourceId = 1L;
        Long targetId = 2L;

        // when
        Favorite result = new Favorite(memberId, sourceId, targetId);

        // then
        assertThat(result.getMemberId()).isEqualTo(memberId);
        assertThat(result.getSourceId()).isEqualTo(sourceId);
        assertThat(result.getTargetId()).isEqualTo(targetId);
    }

    @Test
    void 즐겨찾기를_생성한_유저인지_확인한다() {
        // given
        Long memberId = 1L;
        Long sourceId = 1L;
        Long targetId = 2L;
        Favorite favorite = new Favorite(memberId, sourceId, targetId);

        // when
        boolean result = favorite.isCreatedMember(memberId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 즐겨찾기를_생성한_유저가_아닌지_확인한다() {
        // given
        Long memberId = 1L;
        Long sourceId = 1L;
        Long targetId = 2L;
        Favorite favorite = new Favorite(memberId, sourceId, targetId);
        Long newMemberId = 2L;

        // when
        boolean result = favorite.isCreatedMember(newMemberId);

        // then
        assertThat(result).isFalse();
    }
}
