package atdd.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStationRepository extends JpaRepository<FavoriteStation, Long> {
}
