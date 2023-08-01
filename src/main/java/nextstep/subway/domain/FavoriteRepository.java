package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    public List<Favorite> findByMemberId(Long memberId);
}
