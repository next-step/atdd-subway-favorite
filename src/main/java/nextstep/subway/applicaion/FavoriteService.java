package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final StationService stationService;
    private final PathService pathService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, PathService pathService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.pathService = pathService;
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return favorites.stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FavoriteResponse addFavorite(Long memberId, Long sourceId, Long targetId) {
        validation(sourceId, targetId);
        Station sourceStation = stationService.findById(sourceId);
        Station targetStation = stationService.findById(targetId);
        pathChecker(sourceStation, targetStation);
        Favorite favorite = favoriteRepository.save(new Favorite(memberId, sourceStation, targetStation));

        return createFavoriteResponse(favorite);
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget())
        );
    }

    private void validation(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수는 없습니다.");
        }
    }

    private void pathChecker(Station sourceStation, Station targetStation) {
        pathService.searchPath(sourceStation, targetStation);
    }
}
