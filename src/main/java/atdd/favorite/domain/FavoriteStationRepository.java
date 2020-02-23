package atdd.favorite.domain;

import atdd.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteStationRepository extends JpaRepository<FavoriteStation, Long> {
    @Query(value = "SELECT * FROM favorite_station WHERE user_email=?", nativeQuery = true)
    List<FavoriteStation> findAllByEmail(String email);
}
