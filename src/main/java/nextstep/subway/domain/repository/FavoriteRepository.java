package nextstep.subway.domain.repository;

import lombok.NonNull;
import nextstep.subway.domain.entity.favorite.Favorite;
import nextstep.subway.domain.exception.NotFoundFavoriteException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMemberId(Long memberId);

    @NonNull
    default Favorite findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundFavoriteException(id));
    }
}
