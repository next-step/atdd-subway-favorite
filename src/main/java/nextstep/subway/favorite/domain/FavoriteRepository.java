package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMemberId(Long memberId);

    Optional<Favorite> findByMemberIdAndId(Long memberId, Long id);

    List<Favorite> findAllBySourceId(Long sourceId);
    List<Favorite> findAllByTargetId(Long targetId);

    @Override
    void deleteAll(Iterable<? extends Favorite> entities);

}
