package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.common.exception.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
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

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = findMemberByIdOrThrow(loginMember);
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        return favorites.stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long favoriteId, LoginMember loginMember) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new FavoriteNotFoundException(favoriteId));

        Member member = findMemberByIdOrThrow(loginMember);

        if (!favorite.getMemberId().equals(member.getId())) {
            throw new UnauthorizedDeletionException(favoriteId);
        }

        favoriteRepository.deleteById(favoriteId);
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
