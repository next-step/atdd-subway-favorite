package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final StationService stationService;
    private final PathService pathService;

    public FavoriteService(StationService stationService, PathService pathService) {
        this.stationService = stationService;
        this.pathService = pathService;
    }

    @Transactional
    public FavoriteResponse addFavorite(Long memberId, Long sourceId, Long targetId) {
        validation(sourceId, targetId);
        Station sourceStation = stationService.findById(sourceId);
        Station targetStation = stationService.findById(targetId);
        pathChecker(sourceStation, targetStation);
        Favorite favorite = new Favorite(memberId, sourceStation, targetStation);

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
