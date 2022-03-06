package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final StationRepository stationRepository;

	public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
		this.favoriteRepository = favoriteRepository;
		this.stationRepository = stationRepository;
	}

	public FavoriteResponse addFavorite(Long memberId, FavoriteRequest favoriteRequest) {
		Station source = stationRepository.getById(favoriteRequest.getSourceId());
		Station target = stationRepository.getById(favoriteRequest.getTargetId());

		validateExists(memberId, source, target);

		Favorite favorite = favoriteRepository.save(new Favorite(memberId, source, target));

		return FavoriteResponse.from(favorite);
	}

	private void validateExists(Long memberId, Station source, Station target) {
		boolean existsFavorite = favoriteRepository.findAllByMemberId(memberId).stream()
				.filter(favorite -> favorite.getSource().equals(source))
				.anyMatch(favorite -> favorite.getTarget().equals(target));

		if (existsFavorite) {
			throw new IllegalArgumentException("동일한 즐겨찾기가 등록되어 있습니다.");
		}
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponse> findAllFavorite() {
		List<Favorite> favorites = favoriteRepository.findAll();

		return favorites.stream()
				.map(FavoriteResponse::from)
				.collect(Collectors.toList());
	}

	public void deleteFavorite(Long MemberId, Long favoriteId) {
		favoriteRepository.deleteById(favoriteId);
	}
}
