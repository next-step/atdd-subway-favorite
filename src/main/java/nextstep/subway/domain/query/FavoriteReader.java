package nextstep.subway.domain.query;

import nextstep.subway.domain.entity.favorite.Favorite;
import nextstep.subway.domain.repository.FavoriteRepository;
import nextstep.subway.domain.view.FavoriteView;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteReader {
    private FavoriteRepository favoriteRepository;

    public FavoriteReader(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteView.Main getById(Long id) {
        return null;
    }

    public List<FavoriteView.Main> getFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return null;
    }

}
