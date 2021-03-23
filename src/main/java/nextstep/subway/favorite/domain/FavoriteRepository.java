package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    public List<Favorite> findByUserId(final Long userId);

    public Optional<Favorite> findByIdAndUserId(final Long id, final Long userId);
}

