package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    public FavoriteResponse createFavorite(String email, FavoriteRequest request) {
        return null;
    }

    public List<FavoriteResponse> findFavorites(String email) {
        return null;
    }

    public void deleteFavorite(String email, Long favoriteId) {

    }
}
