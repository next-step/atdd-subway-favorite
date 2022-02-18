package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsFavoriteBySourceIdAndTargetId(Long sourceId, Long targetId);
    void deleteByIdAndMemberId(Long id, Long memberId);
}
