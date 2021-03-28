package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FavoriteRepositoryTest {

    private static final long MEMBER_ID = 1L;

    @Autowired
    private FavoriteRepository favoriteRepository;

    Station source;
    Station target1;
    Station target2;

    @BeforeEach
    void setUp() {
        source = new Station("강남역");
        target1 = new Station("양재역");
        target2 = new Station("청계산입구역");
    }

    @Test
    @DisplayName("MemberId에 포함된 즐겨찾기 조회")
    void findAllByMemberId() {
        // given
        Favorite savedFavorite1 = favoriteRepository.save(new Favorite(MEMBER_ID, source, target1));
        Favorite savedFavorite2 = favoriteRepository.save(new Favorite(MEMBER_ID, source, target2));

        // when
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(MEMBER_ID);

        // then
        assertThat(favorites).hasSize(2);
        assertThat(favorites).containsAll(Arrays.asList(savedFavorite1, savedFavorite2));
    }

    @Test
    @DisplayName("MemberId에 포함된 즐겨찾기 중 이미 포함된 즐겨찾기 확인")
    void validateFavoriteAlready() {
        // given
        favoriteRepository.save(new Favorite(MEMBER_ID, source, target1));

        // when
        boolean result = favoriteRepository.existsByMemberIdAndSourceAndTarget(MEMBER_ID, source, target1);

        // then
        assertThat(result).isTrue();
    }
}
