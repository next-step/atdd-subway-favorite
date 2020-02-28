package atdd.favorite.service;

import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.domain.FavoriteStation;
import atdd.favorite.domain.FavoriteStationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.Optional;

@Service
public class FavoriteStationService {
    private FavoriteStationRepository favoriteStationRepository;

    public FavoriteStationService(FavoriteStationRepository favoriteStationRepository) {
        this.favoriteStationRepository = favoriteStationRepository;
    }

    public Optional<FavoriteStationResponseView> create(CreateFavoriteStationRequestView requestView) {
        Optional<FavoriteStation> existingFavoriteStation
                = favoriteStationRepository.findByStationId(requestView.getStationId());
        if(existingFavoriteStation.isPresent()){
            return null;
        }
        FavoriteStation savedFavoriteStation
                = favoriteStationRepository.save(FavoriteStation.of(requestView));
        return Optional.of(FavoriteStationResponseView.of(savedFavoriteStation));
    }
}
