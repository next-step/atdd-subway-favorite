package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse create(Long sourceId, Long targetId, Long memberId) {
        final Station source = findStationById(sourceId);
        final Station target = findStationById(targetId);

        final Favorite favorite = favoriteRepository.save(new Favorite(source, target, memberId));

        return new FavoriteResponse(favorite.getId());
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllByMemberId(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void delete(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

    private Station findStationById(Long sourceId) {
        return stationRepository.findById(sourceId)
                .orElseThrow(RuntimeException::new);
    }
}
