package nextstep.member.application;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

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
		Favorite favorite = favoriteRepository.save(new Favorite(memberId, request.getSource(), request.getTarget()));

		Station source = stationService.findById(request.getSource());
		Station target = stationService.findById(request.getTarget());

		return FavoriteResponse.from(favorite.getId(), source, target);
	}

	public List<FavoriteResponse> findByMemberId(Long memberId) {
		List<FavoriteResponse> favoriteResponses = new ArrayList<>();

		favoriteRepository.findByMemberId(memberId)
			.stream()
			.forEach(it -> {
				Station source = stationService.findById(it.getSource());
				Station target = stationService.findById(it.getTarget());

				favoriteResponses.add(FavoriteResponse.from(it.getId(), source, target));
			});

		return favoriteResponses;
	}

	public void deleteById(Long memberId, Long favoriteId) {
		Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(EntityNotFoundException::new);

		favorite.validateMember(memberId);

		favoriteRepository.deleteById(favoriteId);
	}
}
