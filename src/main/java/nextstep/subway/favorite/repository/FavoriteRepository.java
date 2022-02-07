package nextstep.subway.favorite.repository;

import nextstep.subway.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
