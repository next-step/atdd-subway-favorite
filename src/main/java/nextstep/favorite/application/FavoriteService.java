package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.FavoriteException;
import nextstep.member.application.MemberService;
import nextstep.auth.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.path.PathService;
import nextstep.subway.station.Station;
import nextstep.subway.station.service.StationDataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final PathService pathService;
    private final StationDataService stationDataService;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            MemberService memberService,
            PathService pathService,
            StationDataService stationDataService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.pathService = pathService;
        this.stationDataService = stationDataService;
    }

    public Long createFavorite(FavoriteRequest request, LoginMember loginMember) {
        verifyDisConnectedStations(request);

        Member member = memberService.findMemberByEmail(loginMember.getEmail());

        Favorite favorite = new Favorite(
                request.getSource(),
                request.getTarget(),
                member.getId()
        );

        return favoriteRepository.save(favorite).getId();
    }

    private void verifyDisConnectedStations(FavoriteRequest request) {
        pathService.getPath(request.getSource(), request.getTarget());
    }

    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();

        return mappingToFavoriteResponse(favorites);
    }

    private List<FavoriteResponse> mappingToFavoriteResponse(List<Favorite> favorites) {
        return favorites.stream().map(f -> {
                    Station source = stationDataService.findStation(f.getSourceStationId());
                    Station target = stationDataService.findStation(f.getTargetStationId());

                    return FavoriteResponse.ofEntity(f.getId(), source, target);
                })
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        Favorite favorite = favoriteRepository
                .findById(id)
                .orElseThrow(() -> new FavoriteException("존재하지 않는 즐겨찾기입니다."));
        favoriteRepository.delete(favorite);
    }
}
