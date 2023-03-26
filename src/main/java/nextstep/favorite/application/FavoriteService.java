package nextstep.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.CannotDeleteFavorite;
import nextstep.favorite.exception.FavoriteErrorMessage;
import nextstep.favorite.exception.NotFoundException;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.AuthUser;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;

@Service
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final StationService stationService;
	private final MemberService memberService;

	public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService,
		MemberService memberService) {
		this.favoriteRepository = favoriteRepository;
		this.stationService = stationService;
		this.memberService = memberService;
	}

	@Transactional
	public FavoriteResponse saveFavorite(AuthUser authUser, FavoriteRequest request) {
		Member member = memberService.findMemberByEmail(authUser.getEmail());
		Station source = stationService.findById(request.getSource());
		Station target = stationService.findById(request.getTarget());

		Favorite favorite = new Favorite(member, source, target);

		favoriteRepository.save(favorite);

		return FavoriteResponse.of(
			favorite.getId(),
			StationResponse.of(favorite.getSource()),
			StationResponse.of(favorite.getTarget()));
	}

	public List<FavoriteResponse> findAllFavorites(AuthUser authUser) {
		Member member = memberService.findMemberByEmail(authUser.getEmail());

		return favoriteRepository.findAllByMember(member)
			.stream()
			.map(favorite -> FavoriteResponse.of(
				favorite.getId(),
				StationResponse.of(favorite.getSource()),
				StationResponse.of(favorite.getTarget())))
			.collect(Collectors.toList());
	}

	@Transactional
	public void deleteFavoriteById(AuthUser authUser, Long id) {
		Member member = memberService.findMemberByEmail(authUser.getEmail());
		Favorite favorite = getFavoriteById(id);

		if (!favorite.isFavoriteOfMember(member)) {
			throw new CannotDeleteFavorite(FavoriteErrorMessage.SHOULD_BE_OWN_FAVORITES);
		}

		favoriteRepository.deleteById(id);
	}

	private Favorite getFavoriteById(Long id) {
		return favoriteRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(FavoriteErrorMessage.NOT_FOUND_FAVORITE));
	}
}
