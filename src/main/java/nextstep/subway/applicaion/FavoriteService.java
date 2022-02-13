package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Station sourceStation = stationService.findById(request.getSource());
        Station targetStation = stationService.findById(request.getTarget());

        Favorite favorite = new Favorite(memberId, sourceStation.getId(), targetStation.getId());
        favoriteRepository.save(favorite);

        return FavoriteResponse.from(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavoriteResponses(Long memberId) {
        return null;
    }
}
