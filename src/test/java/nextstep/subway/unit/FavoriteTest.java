package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nextstep.subway.domain.Favorite;

public class FavoriteTest {

	private Long memberId = 1L;
	private Long sourceStationId = 1L;
	private Long targetStationId = 3L;

	private Long createMemberId = 1L;
	private Long notCreateMemberId = 2L;

	@Test
	void isCreateByTest() {
		Favorite favorite = new Favorite(memberId, sourceStationId, targetStationId);

		assertThat(favorite.isCreateBy(createMemberId)).isTrue();
		assertThat(favorite.isCreateBy(notCreateMemberId)).isFalse();
	}
}
