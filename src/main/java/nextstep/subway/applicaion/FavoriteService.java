package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    public FavoriteResponse createFavorite(FavoriteRequest favoriteRequest) {
        return null;
    }
}
