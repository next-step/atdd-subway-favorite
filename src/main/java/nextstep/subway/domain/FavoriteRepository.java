package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByMemberId(Long memberId);

    Optional<Favorite> findByIdAndMemberId(Long favoriteId, Long memberId);
}
