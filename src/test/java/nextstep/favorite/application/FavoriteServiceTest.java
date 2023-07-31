package nextstep.favorite.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.auth.AuthenticationException;
import nextstep.auth.BadRequestException;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class FavoriteServiceTest {

    public static final String TEST_EMAIL = "email@email.com";
    public static final long NOT_EXISTS_FAVORITE_ID = 1000000;
    public static final String OTHER_EMAIL = "other@email.com";
    public static final int GYODAE_STATION_ID = 1;
    public static final int YANGJAE_STATION_ID = 3;
    public static final long TEST_FAVORITE_ID = 1L;
    public static final long NOT_EXISTS_STATION_ID = 100000L;

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        stationRepository.save(new Station("교대역"));
        stationRepository.save(new Station("강남역"));
        stationRepository.save(new Station("양재역"));
    }

    @AfterEach
    void clear() {
        stationRepository.deleteAll();
        favoriteRepository.deleteAll();
    }

    @DisplayName("존재하지 않는 즐겨찾기를 delete 요청시 예외가 발생한다")
    @Test
    void deleteNotExistsFavorite() {
        assertThatThrownBy(() -> favoriteService.delete(TEST_EMAIL, NOT_EXISTS_FAVORITE_ID))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("다른 사람의 즐겨찾기를 delete 요청시 예외가 발생한다")
    @Test
    void deleteOthersFavorite() {
        // given
        favoriteService.createFavorite(OTHER_EMAIL, GYODAE_STATION_ID, YANGJAE_STATION_ID);

        // when,then
        assertThatThrownBy(() -> favoriteService.delete(TEST_EMAIL, TEST_FAVORITE_ID))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("존재하지 않는 지하철역 아이디를 포함한 즐겨찾기 요청이면 예외가 발생한다")
    @Test
    void createFavoriteThatContainsNotExistsStationId() {
        Assertions.assertAll(
                () -> assertThatThrownBy(
                        () -> favoriteService.createFavorite(OTHER_EMAIL, NOT_EXISTS_STATION_ID, YANGJAE_STATION_ID))
                        .isInstanceOf(BadRequestException.class),
                () -> assertThatThrownBy(
                        () -> favoriteService.createFavorite(OTHER_EMAIL, YANGJAE_STATION_ID, NOT_EXISTS_STATION_ID))
                        .isInstanceOf(BadRequestException.class)
        );
    }
}
