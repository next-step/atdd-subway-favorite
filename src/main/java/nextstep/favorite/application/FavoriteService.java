package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Favorite favorite = new Favorite(loginMember.getId(), request.getSource(), request.getTarget());
        favoriteRepository.save(favorite);
        return favorite.getId();
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());

        Set<Long> stationIds = favorites.stream().flatMap(this::getStationIds)
                .collect(Collectors.toSet());

        Map<Long, Station> stations = stationService.loadFindStationsIds(stationIds);

        return favorites.stream()
                .map(it -> FavoriteResponse.of(
                        it,
                        StationResponse.of(stations.get(it.getSourceStationId())),
                        StationResponse.of(stations.get(it.getTargetStationId()))))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        favoriteRepository.findById(id).ifPresent(favorite -> {
                    if (!favorite.isCreatedBy(loginMember.getId())) {
                        throw new NoMartianException();
                    }
                }
        );

        favoriteRepository.deleteById(id);
    }

    public Stream<Long> getStationIds(Favorite favorite) {
        Set<Long> ids = new HashSet<>();
        ids.add(favorite.getSourceStationId());
        ids.add(favorite.getTargetStationId());
        return ids.stream();
    }
}
