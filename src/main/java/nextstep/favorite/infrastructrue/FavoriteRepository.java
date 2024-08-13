package nextstep.favorite.infrastructrue;

import java.util.List;
import java.util.Optional;
import nextstep.favorite.domain.Favorite;

public interface FavoriteRepository {

    Favorite save(Favorite favorite);
    List<Favorite> findAll();
    Optional<Favorite> findById(Long id);
    void deleteById(Long id);
    List<Favorite> findByMemberId(Long memberId);
}
