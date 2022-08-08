package nextstep.subway.applicaion;

import java.util.Arrays;
import java.util.List;

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

	public FavoriteService(FavoriteRepository favoriteRepository, SubwayMemberDetailsService subwayMemberDetails) {
		this.favoriteRepository = favoriteRepository;
		this.subwayMemberDetails = subwayMemberDetails;
	}

	@Transactional
	public void registerFavorite(String memberEmail, long source, long target) {

		long memberId = getMemberId(memberEmail);
		
		Favorites favorites = new Favorites(favoriteRepository.findByMemberId(memberId));
		favorites.addFavorite(memberId, source, target);
		favoriteRepository.saveAll(favorites.getValues());

	}

	public void deleteFavorite(long favoriteId) {

	}

	public List<FavoriteResponse> getFavorites() {
		return Arrays.asList(new FavoriteResponse());
	}

	private long getMemberId(String memberEmail) {
		SubwayMember subwayMember = subwayMemberDetails.getMemberIdByEmail(memberEmail);
		return subwayMember.getMemberId();
	}
}
