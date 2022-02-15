package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    List<Favorites> findByMemberId(Long memberId);

    void deleteByIdAndMemberId(Long memberId, Long id);
}
