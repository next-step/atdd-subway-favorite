package nextstep.subway.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.member.domain.Member;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMember(Member member);

    Optional<Favorite> findByIdAndMemberId(Long id, Long memberId);
}
