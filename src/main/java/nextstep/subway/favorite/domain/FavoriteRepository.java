package nextstep.subway.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMemberId(Long memberId);

    boolean existsByIdAndMemberId(Long favoriteId, Long memberId);

    void deleteByIdAndMemberId(Long id, Long memberId);
}
