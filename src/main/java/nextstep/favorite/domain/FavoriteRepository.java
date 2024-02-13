package nextstep.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("select f from Favorite f join fetch f.sourceStation join fetch f.targetStation where f.memberId = :memberId")
    List<Favorite> findAllWithStationsByMember(@Param("memberId") Long memberId);

    default boolean existsByStations(final Long memberId, final Long sourceId, final Long targetId) {
        return this.findByStations(memberId, sourceId, targetId).isPresent();
    }

    @Query("select f from Favorite f where f.memberId = :memberId and f.sourceStation.id = :sourceId and f.targetStation.id = :targetId")
    Optional<Favorite> findByStations(@Param("memberId") Long memberId, @Param("sourceId") Long sourceId, @Param("targetId") Long targetId);

    @Query("select f from Favorite f where f.id = :id and f.memberId = :memberId")
    Optional<Favorite> findByIdAndMember(@Param("id") Long id, @Param("memberId") Long memberId);
}
