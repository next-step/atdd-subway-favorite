package nextstep.subway.favorite.application;

import nextstep.subway.auth.exception.UnauthorizedException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class FavoriteServiceTest {

    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    private Member A유저;
    private Member B유저;

    private Favorite 즐겨찾기1;
    private Favorite 즐겨찾기2;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        A유저 = new Member("a_user@gmail.com", "test1234", 20);
        B유저 = new Member("b_user@gmail.com", "test1234", 30);
        ReflectionTestUtils.setField(A유저, "id", 1L);
        ReflectionTestUtils.setField(B유저, "id", 2L);

        즐겨찾기1 = new Favorite(A유저.getId(), 강남역, 양재역);
        즐겨찾기2 = new Favorite(A유저.getId(), 양재역, 남부터미널역);
    }

    @Test
    void deleteFavoriteWrongFavoriteId() {
        // given
        stationRepository.save(강남역);
        stationRepository.save(양재역);
        favoriteRepository.save(즐겨찾기1);

        // when & then
        assertThatThrownBy(() -> favoriteService.deleteFavorite(A유저.getId(), 즐겨찾기2.getId()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteFavoriteWrongMemberId() {
        // given
        stationRepository.save(강남역);
        stationRepository.save(양재역);
        favoriteRepository.save(즐겨찾기1);

        // when & then
        assertThatThrownBy(() -> favoriteService.deleteFavorite(B유저.getId(), 즐겨찾기1.getId()))
                .isInstanceOf(UnauthorizedException.class);
    }
}