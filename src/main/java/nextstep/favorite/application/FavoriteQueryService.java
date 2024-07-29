package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.payload.FavoriteRequest;
import nextstep.favorite.payload.FavoriteResponse;
import nextstep.station.domain.Station;
import nextstep.station.payload.StationResponse;
import nextstep.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Transactional(readOnly = true)
@Service
public class FavoriteQueryService {
    private final FavoriteRepository favoriteRepository;

    private final StationRepository stationRepository;

    public FavoriteQueryService(final FavoriteRepository favoriteRepository, final StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }


    public List<FavoriteResponse> findFavorites(final Long memberId) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);
        List<Long> stationIds = favorites.stream()
                .flatMap(it -> Stream.of(it.getSourceStationId(), it.getTargetStationId()))
                .collect(Collectors.toList());

        Map<Long, Station> stationMap = getStationMap(stationIds);

        return favorites.stream()
                .map(it -> new FavoriteResponse(
                        it.getId(),
                        StationResponse.from(stationMap.get(it.getSourceStationId())),
                        StationResponse.from(stationMap.get(it.getTargetStationId()))))
                .collect(Collectors.toList());
    }


    private Map<Long, Station> getStationMap(final Collection<Long> stationsIds) {
        return stationRepository.findByIdIn(stationsIds)
                .stream()
                .collect(Collectors.toMap(Station::getId, (station -> station)));
    }

}
