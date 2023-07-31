package nextstep.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public long createFavorite(String email, long source, long target) {
        Station sourceStation = stationRepository.findById(source).orElseThrow();
        Station tartgetStation = stationRepository.findById(target).orElseThrow();
        Favorite favorite = new Favorite(email, sourceStation, tartgetStation);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return savedFavorite.getId();
    }

    public List<FavoriteResponse> getFavorites(String email) {
        List<Favorite> favorites = favoriteRepository.findAllByEmail(email);
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }
}
