package nextstep.subway.favorite.repository;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMemberId(long memberId);

    @Modifying
    @Query("delete from Favorite f where f.id = :id and f.memberId = :memberId")
    void deleteByIdAndMemberId(@Param("id") long id, @Param("memberId") long memberId);

    @Modifying
    @Query("delete from Favorite f where f.source = :station or f.target = :station")
    void deleteByStation(@Param("station") Station station);
}
