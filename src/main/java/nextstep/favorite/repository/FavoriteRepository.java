package nextstep.favorite.repository;

import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    List<Favorite> findByMember(Member member);
}
