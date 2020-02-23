package atdd.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoritePathRepository extends JpaRepository<FavoritePath, Long> {
    @Query(value = "SELECT * FROM favorite_path WHERE user_email=?", nativeQuery = true)
    List<FavoritePath> findAllByEmail(String email);
}
