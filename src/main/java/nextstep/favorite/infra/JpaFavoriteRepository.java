package nextstep.favorite.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;

public interface JpaFavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepository {
}
