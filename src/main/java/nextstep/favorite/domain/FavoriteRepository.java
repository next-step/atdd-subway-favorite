package nextstep.favorite.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    void deleteByIdAndMemberId(Long id, Long memberId);

    List<Favorite> findAllByMemberId(Long memberId);
}
