package nextstep.subway.domain.repository;

import nextstep.subway.domain.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	List<Favorite> findByMemberId(Long memberId);
}
