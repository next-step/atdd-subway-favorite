package atdd.path.application;

import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.domain.FavoriteStation;
import atdd.user.domain.User;
import atdd.path.repository.FavoriteStationRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteStationRepository favoriteStationRepository;

    public FavoriteService(FavoriteStationRepository favoriteStationRepository) {
        this.favoriteStationRepository = favoriteStationRepository;
    }

    public FavoriteResponseView createStationFavorite(Long stationId, User user) {
        FavoriteStation savedFavorite
                = favoriteStationRepository.save(FavoriteStation.builder().userId(user.getId()).stationId(stationId).build());

        return FavoriteResponseView.of(savedFavorite);
    }
}
