package nextstep.api.favorite.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.api.favorite.domain.model.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	List<Favorite> findFavoriteByMemberId(Long memberId);
}
