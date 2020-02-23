package atdd.path.repository;

import atdd.path.domain.FavoriteStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStationRepository extends JpaRepository<FavoriteStation, Long> {
}
