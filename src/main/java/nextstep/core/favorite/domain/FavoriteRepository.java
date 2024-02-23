package nextstep.core.favorite.domain;

import nextstep.core.line.domain.Line;
import nextstep.core.member.domain.Member;
import nextstep.core.section.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("SELECT f FROM Favorite f WHERE f.member = :member")
    List<Favorite> findByMember(@Param("member")Member member);

}
