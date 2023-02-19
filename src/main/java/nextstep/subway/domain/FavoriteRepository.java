package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByIdAndMemberId(final Long id, final Long memberId);

    void deleteByIdAndMemberId(final Long id, final Long memberId);
}
