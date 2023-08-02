package nextstep.member.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteDataRepository extends JpaRepository<FavoriteData, Long> {

    @Query("select favorite,source,target "
            + "from FavoriteData favorite "
            + "join StationData source "
            + "on favorite.source.id = source.id "
            + "join StationData target "
            + "on favorite.target.id = target.id "
            + "where favorite.member = :member")
    List<FavoriteData> findAllByMember(Member member);
}
