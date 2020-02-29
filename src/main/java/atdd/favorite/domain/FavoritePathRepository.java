package atdd.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritePathRepository extends JpaRepository<FavoritePath, Long> {
    List<FavoritePath> findAllByEmail(String email);
}
