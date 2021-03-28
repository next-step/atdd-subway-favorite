package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;

    public FavoriteResponse createFavorite(FavoriteRequest favoriteRequest, Long memberId) {
        return new FavoriteResponse();
    }

    public List<FavoriteResponse> findByMemberId(Long id) {
        return null;
    }

    public void removeFavorite(Long id, Long memberId) {

    }
}
