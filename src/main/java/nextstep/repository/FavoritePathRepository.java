package nextstep.repository;

import nextstep.domain.FavoritePath;
import nextstep.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritePathRepository extends JpaRepository<FavoritePath,Long> {
    List<FavoritePath> findByMember(Member member);
}
