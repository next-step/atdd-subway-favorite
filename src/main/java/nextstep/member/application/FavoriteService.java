package nextstep.member.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;

@Service
@Transactional
public class FavoriteService {
	private FavoriteRepository favoriteRepository;
	private StationService stationService;

	public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
		this.favoriteRepository = favoriteRepository;
		this.stationService = stationService;
	}

	public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest request) {
		Station source = stationService.findById(request.getSource());
		Station target = stationService.findById(request.getTarget());

		return FavoriteResponse.of(favoriteRepository.save(new Favorite(memberId, source, target)));
	}

	public List<FavoriteResponse> findByMemberId(Long memberId) {
		return favoriteRepository.findByMemberId(memberId)
			.stream().map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}
}
