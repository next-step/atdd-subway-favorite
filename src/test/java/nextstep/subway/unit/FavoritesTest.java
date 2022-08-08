package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.Favorites;

public class FavoritesTest {

	@BeforeEach
	void setUp() {

	}

	@Test
	@DisplayName("즐겨찾기 등록")
	void addFavorite() {
		//given
		Favorites favorites = new Favorites(null);

		//when
		favorites.addFavorite(1, 2, 3);
		favorites.addFavorite(1, 5, 7);

		//then
		assertThat(favorites.getValues()).hasSize(2);
	}

	@Test
	@DisplayName("동일 즐겨찾기 등록")
	void addSameFavorite() {
		//given
		Favorites favorites = new Favorites(null);

		//when
		favorites.addFavorite(1, 2, 3);

		//then
		assertThatThrownBy(() -> favorites.addFavorite(1, 2, 3))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("즐겨찾기 삭제")
	void removeFavorite() {
		Favorites favorites = new Favorites(null);

		favorites.addFavorite(1, 2, 3);
		favorites.addFavorite(1, 5, 7);

		favorites.removeFavorite(2, 3);

		assertThat(favorites.getValues()).hasSize(1);
	}

	@Test
	@DisplayName("등록되지 않은 즐겨찾기 삭제")
	void removeNotExistingFavorite() {
		Favorites favorites = new Favorites(null);

		favorites.addFavorite(1, 2, 3);
		favorites.addFavorite(1, 5, 7);

		assertThatThrownBy(() -> favorites.removeFavorite(9, 3))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
