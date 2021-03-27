package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {
//    private FavoriteRepository favoriteRepository;

//    public FavoriteService(FavoriteRepository favoriteRepository) {
//        this.favoriteRepository = favoriteRepository;
//    }

    public List<FavoriteResponse> findFavoritesByMemberId(Long memberId) {
        return null;
    }

    public Long saveFavorite(FavoriteRequest request) {
        return null;
    }

    public void removeFavorite(Long id) {}



}
