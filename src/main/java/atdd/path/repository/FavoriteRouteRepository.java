package atdd.path.repository;

import atdd.path.domain.FavoriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute, Long> {
}
