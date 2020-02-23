package atdd.path.application;

import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.User;
import atdd.path.repository.FavoriteStationRepository;
import atdd.path.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final FavoriteStationRepository favoriteStationRepository;

    public FavoriteService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository,
                           FavoriteStationRepository favoriteStationRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.favoriteStationRepository = favoriteStationRepository;
    }

    public FavoriteResponseView createStationFavorite(String token, Long stationId) {
        String userEmail = jwtTokenProvider.getUserEmail(token);
        User user = userRepository.findUserByEmail(userEmail);

        FavoriteStation savedFavorite
                = favoriteStationRepository.save(FavoriteStation.builder().userId(user.getId()).stationId(stationId).build());

        return FavoriteResponseView.of(savedFavorite);
    }
}
