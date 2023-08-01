package nextstep.favorite.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.EntityManager;
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
    public static final long TEST_FAVORITE_ID = 1L;
    public static final long NOT_EXISTS_STATION_ID = 100000L;

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    EntityManager entityManager;
    Station gyodaeStation;
    Station gangnamStation;
    Station yangjaeStation;
    Long gyodaeStationId;
    Long gangnamStationId;
    Long yangjaeStationId;

    @BeforeEach
    void setUp() {
        gyodaeStation = stationRepository.save(new Station("교대역"));
        gangnamStation = stationRepository.save(new Station("강남역"));
        yangjaeStation = stationRepository.save(new Station("양재역"));
        gyodaeStationId = gyodaeStation.getId();
        gangnamStationId = gangnamStation.getId();
        yangjaeStationId = yangjaeStation.getId();
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
        favoriteService.create(OTHER_EMAIL, gyodaeStationId, yangjaeStationId);

        // when,then
        assertThatThrownBy(() -> favoriteService.delete(TEST_EMAIL, TEST_FAVORITE_ID))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("존재하지 않는 지하철역 아이디를 포함한 즐겨찾기 요청이면 예외가 발생한다")
    @Test
    void createFavoriteThatContainsNotExistsStationId() {
        Assertions.assertAll(
                () -> assertThatThrownBy(
                        () -> favoriteService.create(OTHER_EMAIL, NOT_EXISTS_STATION_ID, gyodaeStationId))
                        .isInstanceOf(BadRequestException.class),
                () -> assertThatThrownBy(
                        () -> favoriteService.create(OTHER_EMAIL, yangjaeStationId, NOT_EXISTS_STATION_ID))
                        .isInstanceOf(BadRequestException.class)
        );
    }
}
