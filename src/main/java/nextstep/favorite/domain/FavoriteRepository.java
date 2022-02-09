package nextstep.favorite.domain;

import nextstep.subway.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
