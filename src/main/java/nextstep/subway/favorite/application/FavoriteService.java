package nextstep.subway.favorite.application;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
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
}
