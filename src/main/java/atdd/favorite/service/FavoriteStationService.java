package atdd.favorite.service;

import atdd.favorite.application.dto.FavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationListResponseVIew;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.domain.FavoriteStation;
import atdd.favorite.domain.FavoriteStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteStationService {
    private FavoriteStationRepository favoriteStationRepository;

    public FavoriteStationService(FavoriteStationRepository favoriteStationRepository) {
        this.favoriteStationRepository = favoriteStationRepository;
    }

    public FavoriteStationResponseView create(FavoriteStationRequestView requestView) {
        Optional<FavoriteStation> existingFavoriteStation
                = favoriteStationRepository.findByStationId(requestView.getStationId());
        if (existingFavoriteStation.isPresent()) {
            return null;
        }
        FavoriteStation savedFavoriteStation
                = favoriteStationRepository.save(FavoriteStation.of(requestView));
        return FavoriteStationResponseView.of(savedFavoriteStation);
    }

    public Long delete(FavoriteStationRequestView requestView) throws Exception {
        Optional<FavoriteStation> stationFindById
                = favoriteStationRepository.findById(requestView.getStationId());
        if (stationFindById.isPresent()) {
            favoriteStationRepository.delete(stationFindById.get());
        }


        return requestView.getStationId();
    }

    public FavoriteStationListResponseVIew showAllFavoriteStations(String email) {
        List<FavoriteStation> allByEmail = favoriteStationRepository.findAllByEmail(email);
        return FavoriteStationListResponseVIew.of(email, allByEmail);
    }
}
