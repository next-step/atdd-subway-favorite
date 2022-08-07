package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final StationRepository stationRepository;

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationRepository stationRepository,
                           FavoriteRepository favoriteRepository) {
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Favorite saveFavorite(FavoriteRequest favoriteRequest) {
        Long sourceId = favoriteRequest.getSource();
        Long targetId = favoriteRequest.getTarget();
        Station source = findStation(sourceId);
        Station target = findStation(targetId);
        Favorite favorite = new Favorite(source, target);
        return favoriteRepository.save(favorite);
    }

    private Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다. : " + id));
    }

    public List<FavoriteResponse> findFavorites() {
        return favoriteRepository.findAll().stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}
