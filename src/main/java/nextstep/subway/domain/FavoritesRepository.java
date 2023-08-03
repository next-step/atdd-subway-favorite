package nextstep.subway.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorite, Long> {

    Optional<List<Favorite>> findAllByMemberId(Long memberId);
}
