package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    List<Favorite> findByMember(Member member);
}
