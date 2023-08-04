package nextstep.subway.domain.entity;

import nextstep.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMember(Member member);

    void deleteFavoriteByIdAndMember(Long id, Member member);
}
