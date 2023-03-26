package nextstep.favorite.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nextstep.member.domain.Member;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	List<Favorite> findAllByMember(Member member);
}
