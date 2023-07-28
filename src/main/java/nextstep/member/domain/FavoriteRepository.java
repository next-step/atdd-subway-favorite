package nextstep.member.domain;

import nextstep.subway.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMemberId(Long memberId);

    void deleteByIdAndMemberId(Long id, Long memberId);

    Optional<Favorite> findBySourceAndTargetAndMemberId(Station source, Station target, Long memberId);
}
