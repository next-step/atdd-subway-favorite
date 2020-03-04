package atdd.path.repository;

import atdd.path.domain.FavoriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute, Long> {
    List<FavoriteRoute> findAllByUserId(Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
