package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsFavoriteBySourceAndTarget(Station source, Station target);
    void deleteByIdAndMemberId(Long id, Long memberId);
    List<Favorite> findAllByMemberId(Long memberId);
}
