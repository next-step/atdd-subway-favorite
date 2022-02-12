package nextstep.favorite.domain.repository;

import java.util.List;
import java.util.Optional;

import nextstep.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMemberId(long memberId);

    void deleteByIdAndMemberId(long id, long memberId);
}
