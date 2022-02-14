package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.manager.FavoriteStationService;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteStationService favoriteStationService;

    public FavoriteService(FavoriteRepository favoriteRepository, FavoriteStationService favoriteStationService) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteStationService = favoriteStationService;
    }

    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Favorite favorite = new Favorite(loginMember.getId(), request.getSource(), request.getTarget());
        favoriteRepository.save(favorite);
        return favorite.getId();
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());

        Set<Long> stationIds = favorites.stream().flatMap(section -> section.getStationIds().stream())
                .collect(Collectors.toSet());

        Map<Long, Station> stations = favoriteStationService.loadFindStationsIds(stationIds);

        return favorites.stream()
                .map(it -> FavoriteResponse.of(
                        it,
                        StationResponse.of(stations.get(it.getSourceStationId())),
                        StationResponse.of(stations.get(it.getTargetStationId()))))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!favorite.isCreatedBy(loginMember.getId())) {
            throw new RuntimeException();
        }
        favoriteRepository.deleteById(id);
    }
}
