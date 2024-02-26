package subway.favorite;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dto.favorite.FavoriteRequest;
import subway.dto.favorite.FavoriteResponse;
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
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(
		MemberService memberService,
		StationService stationService,
		PathService pathService,
		FavoriteRepository favoriteRepository
	) {
		this.memberService = memberService;
		this.stationService = stationService;
		this.pathService = pathService;
		this.favoriteRepository = favoriteRepository;
	}

	private Station findStationById(Long stationId) {
		return stationService.findStationById(stationId);
	}

	private Member findMemberByEmail(String email) {
		return memberService.findMemberByEmail(email);
	}

	@Transactional
	public FavoriteResponse save(String email, FavoriteRequest request) {
		Member member = findMemberByEmail(email);
		Station sourceStation = findStationById(request.getSource());
		Station targetStation = findStationById(request.getTarget());

		checkConnectedPath(sourceStation, targetStation);

		Favorite savedFavorite = addFavorite(member, sourceStation, targetStation);

		return FavoriteResponse.of(savedFavorite);
	}

	private Favorite addFavorite(Member member, Station sourceStation, Station targetStation) {
		Favorite favorite = new Favorite(sourceStation, targetStation);
		member.addFavorite(favorite);
		return favoriteRepository.save(favorite);
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

	@Transactional
	public void delete(String email, Long favoriteId) {
		Member member = findMemberByEmail(email);
		Favorite deleteFavorite = findById(favoriteId);
		member.remove(deleteFavorite);
	}

	private Favorite findById(Long favoriteId) {
		return favoriteRepository.findById(favoriteId)
			.orElseThrow(EntityNotFoundException::new);
	}
}
