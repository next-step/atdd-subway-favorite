package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FavoriteRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    public void saveFavorite(FavoriteRequest favoriteRequest) {
        Long source = favoriteRequest.getSource();
        Long target = favoriteRequest.getTarget();
    }
}
