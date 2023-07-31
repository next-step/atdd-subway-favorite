package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public long createFavorite(String email, long source, long target) {
        Favorite favorite = new Favorite(email, source, target);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return savedFavorite.getId();
    }
}
