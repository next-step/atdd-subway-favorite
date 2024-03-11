package nextstep.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByMemberIdAndSourceStationIdAndTargetStationId(Long memberId, Long sourceStationId, Long targetStationId);

    List<Favorite> findAllByMemberId(Long id);
}
