package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritePathRepository extends JpaRepository<FavoritePath, Long> {
    List<FavoritePath> findAllByMember(Member member);
}
