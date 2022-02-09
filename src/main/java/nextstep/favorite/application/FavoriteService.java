package nextstep.favorite.application;

import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.dto.FavoriteRequest;
import nextstep.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse saveFavorite(FavoriteRequest request) {
        return FavoriteResponse.from(favoriteRepository.save(request.toEntity()));
    }

}
