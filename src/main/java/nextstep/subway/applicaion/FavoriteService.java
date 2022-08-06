package nextstep.subway.applicaion;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.auth.user.UserDetails;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(final MemberRepository memberRepository, final FavoriteRepository favoriteRepository, final StationService stationService) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse createFavorite(final UserDetails userDetails, final FavoriteRequest favoriteRequest) {
        final Member member = getMember(userDetails);

        final Favorite save = favoriteRepository.save(new Favorite(member, favoriteRequest.getSource(), favoriteRequest.getTarget()));

        final StationResponse source = getFavoriteStation(save.getSource());
        final StationResponse target = getFavoriteStation(save.getTarget());

        return new FavoriteResponse(save.getId(), source, target);
    }

    private StationResponse getFavoriteStation(final Long save) {
        return StationResponse.of(stationService.findById(save));
    }

    private Member getMember(final UserDetails userDetails) {
        return memberRepository.findByEmail(userDetails.getEmail())
            .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));
    }

    public List<FavoriteResponse> getFavorites(final UserDetails userDetails) {
        final Member member = getMember(userDetails);

        final List<Favorite> favorites = favoriteRepository.findAllByMember(member);

        final Map<Long, Station> stations = favoriteStations(favorites);

        return favorites.stream()
            .map(favorite -> new FavoriteResponse(favorite.getId(),
                    StationResponse.of(stations.get(favorite.getSource())),
                    StationResponse.of(stations.get(favorite.getTarget()))
                )
            )
            .collect(Collectors.toList());
    }

    private Map<Long, Station> favoriteStations(final List<Favorite> favorites) {
        return stationService.findAllStations(extractStationId(favorites)).stream()
            .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private Set<Long> extractStationId(final List<Favorite> favorites) {
        Set<Long> ids = new HashSet<>();
        for (Favorite favorite : favorites) {
            ids.add(favorite.getSource());
            ids.add(favorite.getTarget());
        }
        return ids;
    }

    @Transactional
    public void removeFavorites(final UserDetails userDetails, final long id) {
        final Member member = getMember(userDetails);

        final Favorite favorite = favoriteRepository.findByIdAndMember(id, member)
            .orElseThrow(() -> new NotFoundException("즐겨찾기 정보를 찾을 수 없습니다."));

        favoriteRepository.delete(favorite);
    }

}
