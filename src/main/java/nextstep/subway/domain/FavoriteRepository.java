package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMemberId(Long memberId);
    Optional<Favorite> findByMemberIdAndSourceStationIdAndTargetStationId(final long memberId, final long sourceStationId, final long targetStationId);
}
