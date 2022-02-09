package nextstep.subway.repository;

import nextstep.subway.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Override
    Optional<Favorite> findById(Long id);

    List<Favorite> findByMemberId(long memberId);

}
