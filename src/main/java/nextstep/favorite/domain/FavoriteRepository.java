package nextstep.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByMemberIdAndSourceStationIdAndTargetStationId(Long memberId, Long sourceId, Long targetId);

    List<Favorite> findAllByMemberId(Long memberId);
}
