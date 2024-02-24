package subway.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dto.favorite.FavoriteRequest;
import subway.member.Member;
import subway.member.MemberService;
import subway.path.PathService;
import subway.station.Station;
import subway.station.StationService;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
	private final MemberService memberService;
	private final StationService stationService;
	private final PathService pathService;

	public FavoriteService(MemberService memberService, StationService stationService, PathService pathService) {
		this.memberService = memberService;
		this.stationService = stationService;
		this.pathService = pathService;
	}

	private Station findStationById(Long stationId) {
		return stationService.findStationById(stationId);
	}

	@Transactional
	public Long save(String email, FavoriteRequest request) {
		Member member = memberService.findMemberByEmail(email);
		Station sourceStation = findStationById(request.getSource());
		Station targetStation = findStationById(request.getTarget());

		checkConnectedPath(sourceStation, targetStation);

		member.addFavorite(new Favorite(member, sourceStation, targetStation));
		return member.getFavoriteList()
			.get(0)
			.getId();
	}

	private void checkConnectedPath(Station sourceStation, Station targetStation) {
		pathService.findShortestPath(sourceStation, targetStation);
	}

	public List<FavoriteResponse> findFavorite(String email) {
		Member member = findMemberByEmail(email);
		return member.getFavoriteList()
			.stream()
			.map(FavoriteResponse::of)
			.collect(toList());
	}

}
