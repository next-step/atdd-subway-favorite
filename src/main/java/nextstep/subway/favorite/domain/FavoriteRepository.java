package nextstep.subway.favorite.domain;

import java.util.Optional;

public interface FavoriteRepository {
    Favorite save(Favorite favorite);
    Optional<Favorite> findById(Long id);
}
