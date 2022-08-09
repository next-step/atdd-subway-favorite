package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Override
    <S extends Favorite> S save(S entity);

    List<Favorite> findAllByMemberId(long memberId);

    @Override
    Optional<Favorite> findById(Long aLong);

    @Override
    void delete(Favorite entity);
}
