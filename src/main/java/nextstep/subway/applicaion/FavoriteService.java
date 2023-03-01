package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public long createFavorite(FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findById(Long.parseLong(favoriteRequest.getSource()));
        Station targetStation = stationService.findById(Long.parseLong(favoriteRequest.getSource()));
        Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation));
        return favorite.getId();
    }
}
