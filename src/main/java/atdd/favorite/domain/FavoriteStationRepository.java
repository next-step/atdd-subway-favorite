package atdd.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteStationRepository extends JpaRepository<FavoriteStation, Long> {
    List<FavoriteStation> findAllByEmail(String email);

    Optional<FavoriteStation> findByStationId(Long stationId);
}
