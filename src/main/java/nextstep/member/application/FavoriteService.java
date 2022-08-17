package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.FavoriteRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    public Long saveFavorite(FavoriteRequest favoriteRequest) {
        return null;
    }

    public List<FavoriteResponse> findFavoriteResponses() {
        return null;
    }

    public void deleteFavorite(Long favoriteId) {
        // TODO document why this method is empty
    }
}
