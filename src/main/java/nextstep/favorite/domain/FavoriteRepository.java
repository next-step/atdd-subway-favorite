package nextstep.favorite.domain;

import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query(
        " select new nextstep.favorite.application.dto.FavoriteResponse(" +
        " f.id, s1.id, s1.name, s2.id, s2.name " +
        " )" +
        " FROM Favorite f JOIN Station s1 ON f.sourceStationId = s1.id " +
        " JOIN Station s2 ON f.targetStationId = s2.id " +
        " where f.memberId = :memberId ")
    List<FavoriteResponse> findAllByMemberId(Long memberId);
}
