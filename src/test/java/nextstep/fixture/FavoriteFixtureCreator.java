package nextstep.fixture;

import nextstep.api.favorite.application.model.dto.FavoriteCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/02/12
 */
public class FavoriteFixtureCreator {

	public static FavoriteCreateRequest createFavoriteCreateRequest(Long source, Long target) {
		return FavoriteCreateRequest.builder().sourceStationId(source).targetStationId(target).build();
	}
}
