package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.payload.FavoriteResponse;
import nextstep.station.payload.StationMapper;
import nextstep.station.payload.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional(readOnly = true)
@Service
public class FavoriteQueryService {
    private final FavoriteRepository favoriteRepository;

    private final StationMapper stationMapper;


    public FavoriteQueryService(final FavoriteRepository favoriteRepository, final StationMapper stationMapper) {
        this.favoriteRepository = favoriteRepository;
        this.stationMapper = stationMapper;
    }

    public List<FavoriteResponse> findFavorites(final Long memberId) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);
        List<Long> stationIds = favorites.stream()
                .flatMap(it -> Stream.of(it.getSourceStationId(), it.getTargetStationId()))
                .collect(Collectors.toList());

        Map<Long, StationResponse> stationMap = stationMapper.getStationResponseMap(stationIds);

        return favorites.stream()
                .map(it -> new FavoriteResponse(
                        it.getId(),
                        stationMap.get(it.getSourceStationId()),
                        stationMap.get(it.getTargetStationId())))
                .collect(Collectors.toList());
    }

}
