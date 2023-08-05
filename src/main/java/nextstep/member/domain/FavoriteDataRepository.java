package nextstep.member.domain;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteDataRepository extends JpaRepository<FavoriteData, Long> {

    @EntityGraph(
            attributePaths = {"source", "target", "member"}
    )
    List<FavoriteData> findAllByMember(Member member);
}
