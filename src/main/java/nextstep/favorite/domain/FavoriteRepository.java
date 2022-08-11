package nextstep.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
