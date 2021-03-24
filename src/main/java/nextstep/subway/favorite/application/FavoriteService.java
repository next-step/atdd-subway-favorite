package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    public void createFavorite(Long memberId, FavoriteRequest favoriteRequest) {

    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        return null;
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {

    }
}
