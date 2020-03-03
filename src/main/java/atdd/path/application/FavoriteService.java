package atdd.path.application;

import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.domain.FavoriteStation;
import atdd.path.repository.FavoriteStationRepository;
import atdd.user.domain.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteStationRepository favoriteStationRepository;

    public FavoriteService(FavoriteStationRepository favoriteStationRepository) {
        this.favoriteStationRepository = favoriteStationRepository;
    }

    public FavoriteResponseView createStationFavorite(Long stationId, User user) {
        FavoriteStation savedFavorite = favoriteStationRepository.save(FavoriteStation.builder()
                .userId(user.getId())
                .stationId(stationId)
                .build());

        return FavoriteResponseView.of(savedFavorite);
    }

    public List<FavoriteResponseView> findFavoriteStation(User user) {
        List<FavoriteStation> favorites = favoriteStationRepository.findAllByUserId(user.getId());

        return FavoriteResponseView.listOf(favorites);
    }

    @Transactional
    public void deleteFavoriteStation(User user, Long id) {
        favoriteStationRepository.deleteByIdAndUserId(id, user.getId());
    }
}
