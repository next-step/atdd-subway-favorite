package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    List<Favorite> findAllByMemberId(Long id);

    Optional<Favorite> findByIdAndMemberId(Long favoriteId, Long memberId);
}
