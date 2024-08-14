package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.exception.NotExistFavoriteException;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.path.application.PathService;
import nextstep.station.application.StationService;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;
    private final PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
        this.pathService = pathService;
    }

    @Transactional
    public FavoriteResponse createFavorite(String memberEmail, FavoriteRequest favoriteRequest) {
        pathService.validatePaths(favoriteRequest.getSource(), favoriteRequest.getTarget());

        Member member = memberService.findMemberByEmail(memberEmail);

        Favorite favorite = favoriteRepository.save(new Favorite(favoriteRequest.getSource(), favoriteRequest.getTarget(), member.getId()));

        Station source = stationService.lookUp(favoriteRequest.getSource());
        Station target = stationService.lookUp(favoriteRequest.getTarget());

        return FavoriteResponse.of(favorite.getId(), source, target);
    }

    public List<FavoriteResponse> findFavorites(String memberEmail) {
        Member member = memberService.findMemberByEmail(memberEmail);

        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        return createFavoriteResponses(favorites);
    }

    private List<FavoriteResponse> createFavoriteResponses(List<Favorite> favorites) {
        return favorites.stream()
                .map(it -> FavoriteResponse.of(it.getId(), stationService.lookUp(it.getSourceStationId()), stationService.lookUp(it.getTargetStationId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long id, String memberEmail) {
        Member member = memberService.findMemberByEmail(memberEmail);
        favoriteRepository.findByIdAndMemberId(id, member.getId())
                .orElseThrow(NotExistFavoriteException::new);
        favoriteRepository.deleteById(id);
    }
}
