package nextstep.subway.service;

import nextstep.common.NotFoundMemberException;
import nextstep.common.NotFoundStationException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.controller.request.FavoriteCreateRequest;
import nextstep.subway.controller.resonse.FavoriteResponse;
import nextstep.subway.controller.resonse.PathResponse;
import nextstep.subway.controller.resonse.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.FavoriteRepository;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;

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

        return new FavoriteResponse(
                createdFavorite.getId(),
                new StationResponse(source.getId(), source.getName()),
                new StationResponse(target.getId(), target.getName())
        );
    }

    public void validatePath(Long sourceId, Long targetId) {
        PathResponse shortestPath = pathFindService.getShortestPath(sourceId, targetId);
        if (shortestPath == null) {
            throw new IllegalArgumentException();
        }
    }

}
