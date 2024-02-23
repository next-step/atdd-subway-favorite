package nextstep.api.favorite.application.facade;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.domain.dto.UserPrincipal;
import nextstep.api.favorite.application.model.dto.FavoriteCreateRequest;
import nextstep.api.favorite.application.model.dto.FavoriteCreateResponse;
import nextstep.api.favorite.application.model.dto.FavoriteResponse;
import nextstep.api.favorite.domain.model.dto.inport.FavoriteCreateCommand;
import nextstep.api.favorite.domain.model.dto.outport.FavoriteInfo;
import nextstep.api.favorite.domain.service.FavoriteService;
import nextstep.api.member.application.MemberService;
import nextstep.api.subway.domain.service.LineService;
import nextstep.api.subway.domain.service.impl.StationService;
import nextstep.api.subway.interfaces.dto.response.StationResponse;
import nextstep.common.exception.favorite.FavoriteCreationNotValidException;

/**
 * @author : Rene Choi
 * @since : 2024/02/12
 */
@Component
@RequiredArgsConstructor
public class FavoriteFacade {

	private final FavoriteService favoriteService;
	private final MemberService memberService;
	private final StationService stationService;
	private final LineService lineService;

	public FavoriteCreateResponse create(UserPrincipal loginMember, FavoriteCreateRequest request) {
		if (!lineService.isProperSectionExist(request.getSourceStationId(), request.getTargetStationId())) {
			throw new FavoriteCreationNotValidException();
		}

		return FavoriteCreateResponse.from(favoriteService.create(FavoriteCreateCommand.of(request, fetchMemberId(loginMember))));
	}

	public List<FavoriteResponse> findFavorites(UserPrincipal loginMember) {
		return favoriteService.findFavorites(fetchMemberId(loginMember))
			.stream()
			.map(this::buildFavoriteResponse)
			.collect(Collectors.toList());
	}

	private FavoriteResponse buildFavoriteResponse(FavoriteInfo favoriteInfo) {
		StationResponse source = stationService.findStation(favoriteInfo.getSourceStationId());
		StationResponse target = stationService.findStation(favoriteInfo.getTargetStationId());
		return FavoriteResponse.of(favoriteInfo.getId(), source, target);
	}

	public void deleteFavorite(UserPrincipal loginMember, Long id) {
		favoriteService.deleteFavorite(fetchMemberId(loginMember), id);
	}

	private Long fetchMemberId(UserPrincipal loginMember) {
		return memberService.findMe(loginMember).getId();
	}
}
