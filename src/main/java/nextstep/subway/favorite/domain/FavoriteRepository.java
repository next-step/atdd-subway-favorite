package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMemberId(Long memberId);

    boolean existsByMemberIdAndSourceAndTarget(Long memberId, Station source, Station target);
}
