package nextstep.api.favorite.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.api.favorite.application.model.dto.FavoriteCreateRequest;
import nextstep.api.favorite.domain.model.dto.FavoriteCreateInfo;
import nextstep.api.favorite.domain.model.dto.FavoriteInfo;
import nextstep.api.favorite.domain.model.entity.Favorite;
import nextstep.api.favorite.infrastructure.FavoriteRepository;
import nextstep.common.exception.favorite.FavoriteNotFoundException;
import nextstep.common.exception.member.AuthenticationException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimpleFavoriteService implements FavoriteService {
	private final FavoriteRepository favoriteRepository;

	@Override
	@Transactional
	public FavoriteCreateInfo create(FavoriteCreateRequest request) {
		return FavoriteCreateInfo.from(favoriteRepository.save(Favorite.from(request)));
	}

	@Override
	public List<FavoriteInfo> findFavorites(Long memberId) {
		return favoriteRepository.findFavoriteByMemberId(memberId)
			.stream()
			.map(FavoriteInfo::from)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteFavorite(Long memberId, Long id) {
		Favorite favorite = favoriteRepository.findById(id)
			.orElseThrow(() -> new FavoriteNotFoundException("Favorite not found with id: " + id));

		if (favorite.isNotBelongTo(memberId)) {
			throw new AuthenticationException("Favorite not owned by the member");
		}
		favoriteRepository.deleteById(id);
	}
}
