package nextstep.subway.domain;

import java.util.List;
import java.util.Optional;
import nextstep.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    Optional<List<Favorites>> findAllByMember(Member member);
}
