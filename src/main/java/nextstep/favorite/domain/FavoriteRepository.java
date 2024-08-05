package nextstep.favorite.domain;

import java.util.List;
import nextstep.favorite.exception.FavoriteNotFoundException;
import nextstep.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByMemberAndSourceStationAndTargetStation(Member member, Station sourceStation, Station targetStation);

    List<Favorite> findAllByMember(Member member);

    default Favorite findByIdOrElseThrow(Long id){
        return findById(id)
            .orElseThrow(() -> new FavoriteNotFoundException(id));
    }

}
