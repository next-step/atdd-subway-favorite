package nextstep.subway.applicaion;

import static nextstep.common.ErrorMsg.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.common.exception.AuthorizationException;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final PathService pathService;
	private final StationService stationService;
	private final FavoriteRepository favoriteRepository;

	public Long createFavorite(Long memberId, FavoriteRequest request) {
		pathService.findPath(request.getSource(), request.getTarget());
		return favoriteRepository.save(new Favorite(memberId, request.getSource(), request.getTarget())).getId();
	}

	public List<FavoriteResponse> showFavorite(Long memberId) {
		List<Favorite> favorite = favoriteRepository.findByMemberId(memberId);
		return favorite.stream().map(this::createFavoriteResponse).collect(Collectors.toList());
	}

	public void deleteFavorite(Long memberId, Long id) {
		Favorite favorite = favoriteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(FAVORITE_NOT_FOUND.isMessage()));
		if (!favorite.isCreateBy(memberId)) {
			throw new AuthorizationException(FAVORITE_NOT_MATCH_MEMBER.isMessage());
		}
		favoriteRepository.deleteById(id);
	}

	private FavoriteResponse createFavoriteResponse(Favorite favorite) {
		Long id = favorite.getId();
		Station sourceStation = stationService.findById(favorite.getSourceStationId());
		Station targetStation = stationService.findById(favorite.getTargetStationId());
		return new FavoriteResponse(id, StationResponse.of(sourceStation), StationResponse.of(targetStation));
	}
}
