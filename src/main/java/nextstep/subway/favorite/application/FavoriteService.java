package nextstep.subway.favorite.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.AccessDeniedException;
import nextstep.subway.favorite.exception.NotFoundFavoriteException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class FavoriteService {

	private final StationService stationService;
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(StationService stationService,
		FavoriteRepository favoriteRepository) {
		this.stationService = stationService;
		this.favoriteRepository = favoriteRepository;
	}

	public Long createFavorite(Long memberId, FavoriteRequest request) {
		final Station source = stationService.findStationById(request.getSource());
		final Station target = stationService.findStationById(request.getTarget());

		final Favorite persistFavorite =
			favoriteRepository.save(Favorite.of(memberId, source, target));
		return persistFavorite.getId();
	}

	public List<FavoriteResponse> getFavorites(Long memberId) {
		final List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
		return favorites.stream()
			.map(FavoriteResponse::of)
			.collect(toList());
	}

	public void deleteFavorite(Long memberId, Long favoriteId) {
		final Favorite favorite = favoriteRepository.findById(favoriteId)
			.orElseThrow(NotFoundFavoriteException::new);

		validateAccess(memberId, favorite);

		favoriteRepository.delete(favorite);
	}

	private void validateAccess(Long memberId, Favorite favorite) {
		if (!favorite.isOwner(memberId)) {
			throw new AccessDeniedException();
		}
	}
}
