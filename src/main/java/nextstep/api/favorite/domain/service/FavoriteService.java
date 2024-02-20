package nextstep.api.favorite.domain.service;

import java.util.List;

import nextstep.api.favorite.domain.model.dto.inport.FavoriteCreateCommand;
import nextstep.api.favorite.domain.model.dto.outport.FavoriteCreateInfo;
import nextstep.api.favorite.domain.model.dto.outport.FavoriteInfo;

/**
 * @author : Rene Choi
 * @since : 2024/02/12
 */
public interface FavoriteService {
	FavoriteCreateInfo create(FavoriteCreateCommand request);

	List<FavoriteInfo> findFavorites(Long memberId);

	void deleteFavorite(Long memberId, Long id);
}
