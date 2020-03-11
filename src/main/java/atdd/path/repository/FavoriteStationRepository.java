package atdd.path.repository;

import atdd.path.domain.FavoriteStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteStationRepository extends JpaRepository<FavoriteStation, Long> {
    List<FavoriteStation> findAllByUserId(Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
