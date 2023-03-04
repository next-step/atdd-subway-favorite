package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByMemberIdAndSourceStationIdAndTargetStationId(final long memberId, final long sourceStationId, final long targetStationId);
}
