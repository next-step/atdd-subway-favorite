package atdd.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritePathRepository extends JpaRepository<FavoritePath, Long> {
}
