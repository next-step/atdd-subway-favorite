package nextstep.favorite.infrastructrue;

import nextstep.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long>, FavoriteRepository {

}
