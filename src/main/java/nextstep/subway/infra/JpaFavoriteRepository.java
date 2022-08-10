package nextstep.subway.infra;

import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface JpaFavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepository {
    @Override
    <S extends Favorite> S save(S entity);

    List<Favorite> findAllByMemberId(long memberId);

    @Override
    Optional<Favorite> findById(Long aLong);

    @Override
    void delete(Favorite entity);
}
