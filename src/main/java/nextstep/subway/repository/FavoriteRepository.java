package nextstep.subway.repository;

import nextstep.member.domain.Member;
import nextstep.subway.domain.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	List<Favorite> findByMember(Member member);

	Optional<Favorite> findByIdAndMember(Long id, Member member);
}
