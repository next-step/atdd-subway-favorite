package nextstep.api.favorite.domain.service;

import java.util.List;

import nextstep.api.favorite.application.model.dto.FavoriteCreateRequest;
import nextstep.api.favorite.domain.model.dto.FavoriteCreateInfo;
import nextstep.api.favorite.domain.model.dto.FavoriteInfo;

/**
 * @author : Rene Choi
 * @since : 2024/02/12
 */
public interface FavoriteService {
	FavoriteCreateInfo create(FavoriteCreateRequest request);

	List<FavoriteInfo> findFavorites(Long memberId);

	void deleteFavorite(Long memberId, Long id);
}
