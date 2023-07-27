package nextstep.subway.service;

import nextstep.common.ForbiddenException;
import nextstep.common.NotFoundFavoriteException;
import nextstep.common.NotFoundMemberException;
import nextstep.common.NotFoundStationException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.controller.request.FavoriteCreateRequest;
import nextstep.subway.controller.resonse.FavoriteResponse;
import nextstep.subway.controller.resonse.PathResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.FavoriteRepository;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final PathFindService pathFindService;


    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository, PathFindService pathFindService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.pathFindService = pathFindService;
    }


    public FavoriteResponse createFavorite(String memberEmail, FavoriteCreateRequest request) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new NotFoundMemberException(memberEmail));

        Long sourceId = request.getSource();
        Station source = stationRepository.findById(sourceId)
                .orElseThrow(() -> new NotFoundStationException(sourceId));

        Long targetId = request.getTarget();
        Station target = stationRepository.findById(targetId)
                .orElseThrow(() -> new NotFoundStationException(targetId));

        validatePath(sourceId, targetId);

        Favorite createdFavorite = favoriteRepository.save(Favorite.of(member, source, target));

        return new FavoriteResponse(createdFavorite);
    }

    public void validatePath(Long sourceId, Long targetId) {
        PathResponse shortestPath = pathFindService.getShortestPath(sourceId, targetId);
        if (shortestPath == null) {
            throw new IllegalArgumentException();
        }
    }

    public FavoriteResponse findFavorite(String memberEmail, Long favoriteId) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new NotFoundMemberException(memberEmail));
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NotFoundFavoriteException(favoriteId));

        validateFavoriteOwner(member, favorite);

        return new FavoriteResponse(favorite);
    }

    private void validateFavoriteOwner(Member member, Favorite favorite) {
        if (favorite.isNotOwned(member)) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void deleteFavorite(String memberEmail, Long favoriteId) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new NotFoundMemberException(memberEmail));
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NotFoundFavoriteException(favoriteId));

        validateFavoriteOwner(member, favorite);

        favoriteRepository.delete(favorite);
    }
}
