package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    public FavoriteResponse saveFavorites(FavoriteRequest request) {
        return new FavoriteResponse();
    }
}
