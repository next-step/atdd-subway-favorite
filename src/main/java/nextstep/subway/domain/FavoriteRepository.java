package nextstep.subway.domain;

import java.util.List;
import java.util.Optional;
import nextstep.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByIdAndMember(final Long id, final Member member);

    List<Favorite> findAllByMember(final Member member);
}