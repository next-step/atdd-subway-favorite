package nextstep.favorite.domain;

import nextstep.exception.ApplicationException;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findBySourceStationAndTargetStationAndMember(
            Station sourceStation, Station targetStation, Member member);

    boolean existsBySourceStationAndTargetStationAndMember(
            Station sourceStation, Station targetStation, Member member);

    default Favorite getBy(Station sourceStation, Station targetStation, Member member) {
        return findBySourceStationAndTargetStationAndMember(sourceStation, targetStation, member)
                .orElseThrow(() -> new ApplicationException(
                        "즐겨찾기가 존재하지 않습니다."));
    }

    default void existBy(Station sourceStation, Station targetStation, Member member) {
        if (existsBySourceStationAndTargetStationAndMember(sourceStation, targetStation, member)) {
            throw new ApplicationException("등록되어 있는 즐겨찾기입니다.");
        }
    }
}
