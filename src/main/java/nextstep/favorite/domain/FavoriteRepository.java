package nextstep.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    void deleteByIdAndMemberId(Long id, Long memberId);
}
