package nextstep.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT fv FROM Favorite fv JOIN FETCH fv.member me WHERE me.email = :email")
    List<Favorite> findAllByMemberEmail(String email);
}
