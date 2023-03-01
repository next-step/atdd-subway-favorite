package nextstep.favorite.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    List<Favorite> findByMemberId(Long memberId);
}
