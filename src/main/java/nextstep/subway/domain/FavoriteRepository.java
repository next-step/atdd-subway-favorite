package nextstep.subway.domain;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository {
    <S extends Favorite> S save(S entity);

    List<Favorite> findAllByMemberId(long memberId);

    Optional<Favorite> findById(Long aLong);

    void delete(Favorite entity);
}
