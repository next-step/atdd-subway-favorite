package subway.fixture.favorite;

import subway.dto.favorite.FavoriteRequest;

public class FavoriteRequestFixture {
	public static FavoriteRequest.Builder builder() {
		return FavoriteRequest.builder()
			.source(1L)
			.target(3L);
	}
}
