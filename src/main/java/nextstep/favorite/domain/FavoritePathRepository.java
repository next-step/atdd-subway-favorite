package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritePathRepository extends JpaRepository<FavoritePath, Long> {
    List<FavoritePath> findAllByMember(Member member);

    Optional<FavoritePath> findByIdAndMember(Long favoritePathId, Member member);
}
