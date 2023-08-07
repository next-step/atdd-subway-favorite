package nextstep.subway.domain.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMemberId(Long memberId);

    void deleteFavoriteByIdAndMemberId(Long id, Long memberId);
}
