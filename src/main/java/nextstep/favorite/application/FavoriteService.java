package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.common.exception.MemberNotFoundException;
import nextstep.common.exception.PathNotFoundException;
import nextstep.common.exception.StationNotFoundException;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.infrastructure.FavoriteRepository;
import nextstep.favorite.presentation.FavoriteRequest;
import nextstep.favorite.presentation.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.MemberRepository;
import nextstep.subway.domain.PathFinderService;
import nextstep.subway.domain.Station;
import nextstep.subway.infrastructure.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;
    private final PathFinderService pathFinderService;

    public Long createFavorite(FavoriteRequest request, LoginMember loginMember) {
        Station sourceStation = findStationByIdOrThrow(request.getSourceStationId());
        Station targetStation = findStationByIdOrThrow(request.getTargetStationId());
        Member member = findMemberByIdOrThrow(loginMember);

        if (!pathFinderService.isValidPath(sourceStation.getId(), targetStation.getId())) {
            throw new PathNotFoundException(sourceStation.getId(), targetStation.getId());
        }

        Favorite requestedFavorite = Favorite.of(
                member.getId(),
                sourceStation.getId(),
                targetStation.getId()
        );

        Favorite createdFavorite = favoriteRepository.save(requestedFavorite);
        return createdFavorite.getId();
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = findMemberByIdOrThrow(loginMember);
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        return favorites.stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     *
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    private Station findStationByIdOrThrow(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));
    }

    private Member findMemberByIdOrThrow(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(loginMember.getEmail()));
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        Station sourceStation = findStationByIdOrThrow(favorite.getSourceStationId());
        Station targetStation = findStationByIdOrThrow(favorite.getTargetStationId());
        return FavoriteResponse.of(favorite, sourceStation, targetStation);
    }
}
