package nextstep.subway.applicaion;

import static nextstep.common.ErrorMsg.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final MemberService memberService;
	private final StationService stationService;
	private final FavoriteRepository favoriteRepository;

	public Long createFavorite(String email, FavoriteRequest request) {
		Long memberId = memberService.findByEmail(email).getId();
		Long sourceStationId = stationService.findById(request.getSource()).getId();
		Long targetStationId = stationService.findById(request.getTarget()).getId();
		Long favoriteId = favoriteRepository.save(new Favorite(memberId, sourceStationId, targetStationId)).getId();
		return favoriteId;
	}

	public List<FavoriteResponse> showFavorite(String email) {
		Long memberId = memberService.findByEmail(email).getId();
		List<Favorite> favorite = favoriteRepository.findByMemberId(memberId);
		return favorite.stream().map(this::createFavoriteResponse).collect(Collectors.toList());
	}

	public void deleteFavorite(String email, Long id) {
		Long memberId = memberService.findByEmail(email).getId();
		Favorite favorite = favoriteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(FAVORITE_NOT_FOUND.isMessage()));
		if (!favorite.isCreateBy(memberId)) {
			throw new IllegalArgumentException(FAVORITE_NOT_MATCH_MEMBER.isMessage());
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
