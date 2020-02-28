package atdd.favorite.service;

import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.domain.FavoriteStation;
import atdd.favorite.domain.FavoriteStationRepository;
import atdd.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class FavoriteStationService {
    private FavoriteStationRepository favoriteStationRepository;

    public FavoriteStationService(FavoriteStationRepository favoriteStationRepository) {
        this.favoriteStationRepository = favoriteStationRepository;
    }

    public FavoriteStation create(CreateFavoriteStationRequestView requestView) {
        return new FavoriteStation();
    }
}
