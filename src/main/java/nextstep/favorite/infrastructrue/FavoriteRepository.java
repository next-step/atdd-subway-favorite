package nextstep.favorite.infrastructrue;

import java.util.List;
import nextstep.favorite.domain.Favorite;

public interface FavoriteRepository {

    Favorite save(Favorite favorite);
    List<Favorite> findAll();
    void deleteById(Long id);
    List<Favorite> findByMemberId(Long memberId);
}
