package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {
    private final StationService stationService;
    private final PathService pathService;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, PathService pathService,
        MemberRepository memberRepository, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.pathService = pathService;
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(FavoriteRequest favoriteRequest, Long memberId) {
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        pathService.findPath(source, target);
        Favorite favorite = favoriteRepository.save(Favorite.of(memberId, source, target));
        return FavoriteResponse.from(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return favorites.stream().map(FavoriteResponse::from).collect(Collectors.toList());
    }

    public void deleteFavorite(Long favoriteId, Long memberId) {
        Favorite favorite = favoriteRepository.getById(favoriteId);
        favorite.hasPermission(memberId);
        favoriteRepository.delete(favorite);
    }
}
