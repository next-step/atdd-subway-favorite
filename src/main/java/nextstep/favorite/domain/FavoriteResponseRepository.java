package nextstep.favorite.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteResponseRepository extends JpaRepository<FavoriteResponse, Long> {

    List<FavoriteResponse> findAllByEmail(String email);
}
