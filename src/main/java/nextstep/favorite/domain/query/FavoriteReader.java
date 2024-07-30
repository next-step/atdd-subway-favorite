package nextstep.favorite.domain.query;

import nextstep.favorite.controller.dto.FavoriteResponse;
import nextstep.favorite.domain.entity.Favorite;
import nextstep.favorite.domain.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteReader {
    private FavoriteRepository favoriteRepository;

    public FavoriteReader(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }
    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return null;
    }
}
