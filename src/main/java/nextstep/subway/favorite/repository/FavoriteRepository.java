package nextstep.subway.favorite.repository;

import nextstep.subway.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMemberId(long memberId);

    @Modifying
    @Query("delete from Favorite where id = :id and memberId = :memberId")
    void deleteByIdAndMemberId(@Param("id") long id, @Param("memberId") long memberId);
}
