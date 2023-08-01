package nextstep.api.favorite.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.api.favorite.domain.exception.NoSuchFavoriteException;
import nextstep.api.member.domain.Member;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMember(final Member member);

    void deleteByIdAndMember(final Long id, final Member member);

    default Favorite getById(final Long id) throws NoSuchFavoriteException {
        return findById(id).orElseThrow(() -> NoSuchFavoriteException.from(id));
    }
}
