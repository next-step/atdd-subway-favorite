package nextstep.subway.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.Favorite;

public class FavoriteTest {

	@BeforeEach
	void setUp() {

	}

	@Test
	@DisplayName("동일 즐겨찾기 여부")
	void isSameFavoriteStations() {
		Favorite favorite = new Favorite(1, 2, 3);

		Assertions.assertThat(favorite.isSameFavorite(2, 3))
			.isTrue();
	}

	@Test
	@DisplayName("동일 사용자가 아닌경우")
	void isNotSameMember() {
		Favorite favorite = new Favorite(1, 2, 3);

		Assertions.assertThat(favorite.isNotSameMember(3))
			.isTrue();
	}

}
