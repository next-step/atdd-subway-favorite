package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(FavoriteRequest request) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        Favorite favorite = favoriteRepository.save(new Favorite(source, target));
        return FavoriteResponse.of(favorite);
    }
}
