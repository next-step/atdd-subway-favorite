package nextstep.favorite.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.member.domain.Member;

public interface FavoriteRepository {
    Optional<Favorite> findById(Long id);

    List<Favorite> findByMember(Member member);

    void deleteById(Long id);

    Favorite save(Favorite favorite);
}
