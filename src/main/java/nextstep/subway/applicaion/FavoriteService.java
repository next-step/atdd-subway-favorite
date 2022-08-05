package nextstep.subway.applicaion;

import nextstep.auth.user.User;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.FavoriteValidationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteValidationService favoriteValidationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService,
                           StationService stationService,
                           FavoriteValidationService favoriteValidationService,
                           FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteValidationService = favoriteValidationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(User user, FavoriteRequest request) {
        Long memberId = memberService.findMember(user.getPrincipal()).getId();
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        favoriteValidationService.validateDuplicate(memberId, source.getId(), target.getId());

        Favorite favorite = request.toEntity();
        favorite.toMember(memberId);
        favoriteRepository.save(favorite);
        return new FavoriteResponse(favorite, source, target);
    }

    public List<FavoriteResponse> findFavorites(User user) {
        Long memberId = memberService.findMember(user.getPrincipal()).getId();
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);
        List<Station> favoriteStations = findFavoriteStations(favorites);

        return favorites.stream()
                .map(it -> {
                    Station source = findStationById(favoriteStations, it.getSource());
                    Station target = findStationById(favoriteStations, it.getTarget());
                    return new FavoriteResponse(it, source, target);
                })
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

    private Station findStationById(List<Station> stations, Long id) {
        return stations.stream()
                .filter(it -> it.matchId(id))
                .findAny()
                .orElseThrow();
    }

}
