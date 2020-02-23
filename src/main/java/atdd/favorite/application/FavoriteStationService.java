package atdd.favorite.application;

import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.domain.FavoriteStation;
import atdd.favorite.domain.FavoriteStationRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteStationService {
    private FavoriteStationRepository repository;

    private FavoriteStationService(FavoriteStationRepository repository){
        this.repository=repository;
    }

    public FavoriteStationResponseView createFavoriteStation(CreateFavoriteStationRequestView createRequestView) {
        System.out.println(createRequestView.getUserEmail());
        FavoriteStation createdFavoriteStation=repository.save(createRequestView.toEntity());
        return FavoriteStationResponseView.of(createdFavoriteStation);
    }
}
