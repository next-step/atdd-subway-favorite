package nextstep.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("select f from Favorite f join fetch f.sourceStation join fetch f.targetStation where f.memberId = :memberId")
    List<Favorite> findAllWithStationsByMember(@Param("memberId") Long memberId);
}
