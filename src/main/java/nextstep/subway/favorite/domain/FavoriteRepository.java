package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    public List<Favorite> findByMemberId(final Long memberId);

    public Optional<Favorite> findByIdAndMemberId(final Long id, final Long memberId);
}

