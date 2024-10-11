package nextstep.favorite.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByMemberIdAndSourceIdAndTargetId(Long memberId, Long sourceId, Long targetId);

    List<Favorite> findAllByMemberId(Long memberId);

    Optional<Favorite> findByIdAndMemberId(Long id, Long memberId);
}
