package nextstep.member.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query(value = "SELECT f FROM Favorite f "
            + "INNER JOIN Member m ON f.memberId = m.id "
            + "WHERE m.email = :email")
    List<Favorite> findAllByMemberEmail(String email);

    @Query(value = "SELECT f FROM Favorite f "
            + "INNER JOIN Member m ON f.memberId = m.id "
            + "WHERE f.id = :id AND m.email = :email")
    Optional<Favorite> findByIdAndMemberEmail(String email, Long id);
}
