package subway.favorite;

import org.springframework.stereotype.Service;

import subway.dto.favorite.FavoriteRequest;
import subway.member.Member;
import subway.member.MemberService;
import subway.station.Station;
import subway.station.StationService;

@Service
public class FavoriteService {
	private final MemberService memberService;
	private final StationService stationService;
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(MemberService memberService, StationService stationService,
		FavoriteRepository favoriteRepository) {
		this.memberService = memberService;
		this.stationService = stationService;
		this.favoriteRepository = favoriteRepository;
	}

	public Long save(String email, FavoriteRequest request) {
		Member member = memberService.findMemberByEmail(email);
		Station stationById = stationService.findStationById(request.getSource());
		Station stationById1 = stationService.findStationById(request.getTarget());
		Favorite save = favoriteRepository.save(new Favorite(member, stationById, stationById1));
		return save.getId();
	}
}
