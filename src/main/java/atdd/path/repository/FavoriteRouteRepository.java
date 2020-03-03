package atdd.path.repository;

import atdd.path.domain.FavoriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute, Long> {
    List<FavoriteRoute> findAllByUserId(Long userId);
}
