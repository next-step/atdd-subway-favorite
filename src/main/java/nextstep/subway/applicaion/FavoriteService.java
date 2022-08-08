package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.application.SubwayMemberDetailsService;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Favorites;
import nextstep.subway.domain.SubwayMember;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

	private FavoriteRepository favoriteRepository;
	private SubwayMemberDetails subwayMemberDetails;
	private StationService stationService;

	public FavoriteService(FavoriteRepository favoriteRepository, SubwayMemberDetailsService subwayMemberDetails,
		StationService stationService) {
		this.favoriteRepository = favoriteRepository;
		this.subwayMemberDetails = subwayMemberDetails;
		this.stationService = stationService;
	}

	@Transactional
	public void registerFavorite(String memberEmail, long source, long target) {

		long memberId = getMemberId(memberEmail);
		Favorites favorites = new Favorites(favoriteRepository.findByMemberId(memberId));
		favorites.addFavorite(memberId, getStationId(source), getStationId(target));
		favoriteRepository.saveAll(favorites.getValues());
	}

	@Transactional
	public void deleteFavorite(String memberEmail, long favoriteId) {

		Favorites favorites = new Favorites(favoriteRepository.findByMemberId(getMemberId(memberEmail)));
		favoriteRepository.delete(favorites.getFavoriteById(favoriteId));
	}

	public List<FavoriteResponse> getFavorites(String memberEmail) {
		long memberId = getMemberId(memberEmail);
		Favorites favorites = new Favorites(favoriteRepository.findByMemberId(memberId));
		return createFavoriteResponse(favorites);
	}

	private long getMemberId(String memberEmail) {
		SubwayMember subwayMember = subwayMemberDetails.getMemberIdByEmail(memberEmail);
		return subwayMember.getMemberId();
	}

	private long getStationId(long stationId) {
		return stationService.findById(stationId)
			.getId();
	}

	private List<FavoriteResponse> createFavoriteResponse(Favorites favorites) {
		return favorites.getValues()
			.stream()
			.map(favorite -> new FavoriteResponse(favorite.getId(),
				stationService.createStationResponse(stationService.findById(favorite.getSourceStationId()))
				, stationService.createStationResponse(stationService.findById(favorite.getTargetStationId()))))
			.collect(Collectors.toList());
	}

}
