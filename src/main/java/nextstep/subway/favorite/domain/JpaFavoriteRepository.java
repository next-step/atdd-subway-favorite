package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepository {
}
