package atdd.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritePathRepository extends JpaRepository<FavoritePath, Long> {
    Optional<List<FavoritePath>> findAllByEmail(String email);
}
