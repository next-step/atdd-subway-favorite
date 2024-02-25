package nextstep.favorite.domain;

import java.util.List;
import java.util.Optional;
import nextstep.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  List<Favorite> findAllByMember(Member member);

  Optional<Favorite> findByIdAndMember(Long id, Member member);
}
