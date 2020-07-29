package nextstep.subway.favorite.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByMemberIdAndId(Long memberId, Long Id);

    Optional<List<Favorite>> findAllByMemberId(Long memberId);
}
