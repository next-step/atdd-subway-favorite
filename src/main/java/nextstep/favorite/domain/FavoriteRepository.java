package nextstep.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMemberId(Long memberId);
    Optional<Favorite> findByIdAndMemberId(Long Id, Long memberId);
}

