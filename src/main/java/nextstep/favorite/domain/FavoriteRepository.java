package nextstep.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // public Optional<Favorite> findByIdAndMember(Long favoriteId, Member member);

}
