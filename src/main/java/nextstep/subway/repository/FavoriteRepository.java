package nextstep.subway.repository;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMember(Member member);
}
