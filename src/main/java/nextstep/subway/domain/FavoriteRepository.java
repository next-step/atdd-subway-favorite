package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByMemberId(Long memberId);
}
