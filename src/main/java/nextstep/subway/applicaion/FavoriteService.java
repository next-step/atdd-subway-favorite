package nextstep.subway.applicaion;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.UserDetails;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Favorites;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;

@Service
@Transactional
public class FavoriteService {
	private final MemberRepository memberRepository;
	private final StationRepository stationRepository;
	public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	public FavoriteResponse createFavorites(UserDetails userDetails, FavoriteRequest favoriteRequest) {
		Member member = memberRepository.findById(userDetails.getId()).orElseThrow(EntityNotFoundException::new);
		Station source = stationRepository.getById(favoriteRequest.getSourceId());
		Station target = stationRepository.getById(favoriteRequest.getTargetId());
		Favorite favorite = new Favorite(source,target);
		member.addFavorite(favorite);
		return new FavoriteResponse(favorite);
	}
}
