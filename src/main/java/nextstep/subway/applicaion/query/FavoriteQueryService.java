package nextstep.subway.applicaion.query;

import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteQueryService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteQueryService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteResponse> findFavorite(long loginId) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginId);

        return favorites.stream()
                .map(it -> FavoriteResponse.of(loginId, it.source(), it.target()))
                .collect(Collectors.toList());
    }

}
