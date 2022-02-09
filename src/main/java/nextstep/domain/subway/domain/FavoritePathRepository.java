package nextstep.domain.subway.domain;

import nextstep.domain.member.domain.Member;
import nextstep.domain.subway.dto.PathResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoritePathRepository extends JpaRepository<FavoritePath,Long> {
    @Query("select f from FavoritePath f where f.member = ?1")
    List<FavoritePath> findAllByMember(Member member);

    FavoritePath findOneById(Long favoriteId);
}
