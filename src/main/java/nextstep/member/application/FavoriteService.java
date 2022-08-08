package nextstep.member.application;

import nextstep.auth.user.User;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.station.application.StationService;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(MemberService memberService, StationService stationService) {
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public Long createFavorite(User user, FavoriteRequest request) {
        Member member = memberService.findMember(user.getPrincipal());
        Station sourceStation = stationService.findById(request.getSource());
        Station targetStation = stationService.findById(request.getTarget());

        member.addFavorite(request.toEntity());
        memberService.saveAndFlush(member);
        return member.getFavoriteByStationIds(sourceStation.getId(), targetStation.getId()).getId();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(User user) {
        Member member = memberService.findMember(user.getPrincipal());
        List<Favorite> favorites = member.getFavorites();
        List<Station> favoriteStations = findFavoriteStations(favorites);

        return favorites.stream()
                .map(it -> createFavoriteResponse(it, favoriteStations))
                .collect(Collectors.toList());
    }

    private List<Station> findFavoriteStations(List<Favorite> favorites) {
        List<Long> stationIds = new ArrayList<>();

        favorites.forEach(it -> {
            stationIds.add(it.getSource());
            stationIds.add(it.getTarget());
        });

        return stationService.findByIds(stationIds);
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite, List<Station> favoriteStations) {
        Station source = filterStationsById(favoriteStations, favorite.getSource());
        Station target = filterStationsById(favoriteStations, favorite.getTarget());
        return new FavoriteResponse(favorite, source, target);
    }

    private Station filterStationsById(List<Station> stations, Long id) {
        return stations.stream()
                .filter(it -> it.matchId(id))
                .findAny()
                .orElseThrow();
    }

    public void deleteFavorite(User user, Long id) {
        Member member = memberService.findMember(user.getPrincipal());
        member.deleteFavorite(id);
    }
}
