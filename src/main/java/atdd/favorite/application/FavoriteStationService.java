package atdd.favorite.application;

import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.domain.FavoriteStation;
import atdd.favorite.domain.FavoriteStationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteStationService {
    private FavoriteStationRepository repository;

    private FavoriteStationService(FavoriteStationRepository repository){
        this.repository=repository;
    }

    public FavoriteStationResponseView createFavoriteStation(CreateFavoriteStationRequestView createRequestView) {
        FavoriteStation createdFavoriteStation=repository.save(createRequestView.toEntity());
        return FavoriteStationResponseView.of(createdFavoriteStation);
    }

    public void delete(Long id){
        FavoriteStation favoriteStation=repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));
        repository.delete(favoriteStation);
    }

    public List<FavoriteStation> findAllByEmail(String email){
        List<FavoriteStation> favoriteStations=repository.findAllByEmail(email);
        return favoriteStations;
    }
}
