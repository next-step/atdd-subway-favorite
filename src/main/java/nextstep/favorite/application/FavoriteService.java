package nextstep.favorite.application;

import org.springframework.stereotype.Service;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;

@Service
public class FavoriteService {

	private final MemberService memberService;
	private final StationService stationService;
	private final FavoriteRepository favoriteRepository;
	private final PathService pathService;

	public FavoriteService(MemberService memberService, StationService stationService,
		FavoriteRepository favoriteRepository, PathService pathService) {
		this.memberService = memberService;
		this.stationService = stationService;
		this.favoriteRepository = favoriteRepository;
		this.pathService = pathService;
	}

	public Long create(FavoriteRequest favoriteRequest, String memberEmail) {
		pathService.findPath(favoriteRequest.getSource(), favoriteRequest.getTarget());
		stationService.findById(favoriteRequest.getSource());
		stationService.findById(favoriteRequest.getTarget());
		MemberResponse member = memberService.findMemberByEmail(memberEmail);
		return favoriteRepository.save(favoriteRequest.toFavorite(member.getId()))
			.getId();
	}
}
