package nextstep.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationResponse;

@Service
@Transactional
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

	@Transactional(readOnly = true)
	public List<FavoriteResponse> findAll(String memberEmail) {
		MemberResponse member = memberService.findMemberByEmail(memberEmail);
		return favoriteRepository.findAllByMemberId(member.getId())
			.stream()
			.map(f -> new FavoriteResponse(
					f.getId(),
					StationResponse.from(stationService.findById(f.getSourceStationId())),
					StationResponse.from(stationService.findById(f.getTargetStationId()))
				))
			.collect(Collectors.toUnmodifiableList());
	}

	public void delete(Long id, String memberEmail) {
		MemberResponse member = memberService.findMemberByEmail(memberEmail);
		Favorite favorite = favoriteRepository.findById(id).orElseThrow(IllegalArgumentException::new);

		if (favorite.isNotCreator(member.getId())) {
			throw new IllegalArgumentException();
		}

		favoriteRepository.deleteById(id);
	}
}
