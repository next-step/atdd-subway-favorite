package nextstep.subway.domain.favourite;

import nextstep.subway.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findAllByMember(Member member);
}
