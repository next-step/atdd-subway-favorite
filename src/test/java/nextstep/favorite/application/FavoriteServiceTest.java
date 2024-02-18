package nextstep.favorite.application;

import nextstep.common.fixture.StationFactory;
import nextstep.core.DatabaseCleaner;
import nextstep.core.TestConfig;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.exception.FavoriteNotExistException;
import nextstep.favorite.exception.FavoriteSaveException;
import nextstep.line.application.LineService;
import nextstep.line.application.dto.LineCreateRequest;
import nextstep.member.domain.LoginMember;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
class FavoriteServiceTest {
    @Autowired
    private DatabaseCleaner databaseCleaner;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineService lineService;
    @Autowired
    private FavoriteService favoriteService;

    private LoginMember loginMember;
    private Station 강남역;
    private Station 선릉역;
    private Station 서울역;
    private Station 사당역;

    @BeforeEach
    void setUp() {
        loginMember = new LoginMember(1L, "test@test.com");
        강남역 = stationRepository.save(StationFactory.createStation("강남역"));
        선릉역 = stationRepository.save(StationFactory.createStation("선릉역"));
        서울역 = stationRepository.save(StationFactory.createStation("서울역"));
        사당역 = stationRepository.save(StationFactory.createStation("사당역"));
        lineService.saveLine(new LineCreateRequest("이호선", "연두색", 강남역.getId(), 선릉역.getId(), 10));
        lineService.saveLine(new LineCreateRequest("사호선", "하늘색", 서울역.getId(), 사당역.getId(), 20));
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @Test
    @DisplayName("즐겨찾기를 생성할 수 있다")
    void createFavoriteTest() {
        final FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());

        final FavoriteResponse actual = favoriteService.createFavorite(loginMember, request);

        final FavoriteResponse expected = new FavoriteResponse(actual.getId(), StationResponse.from(강남역), StationResponse.from(선릉역));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경로는 즐겨찾기를 생성할 수 없다")
    void favoriteTargetSourceSameTest() {
        final FavoriteRequest request = new FavoriteRequest(강남역.getId(), 강남역.getId());

        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, request))
                .isInstanceOf(FavoriteSaveException.class)
                .hasMessageContaining("출발역과 도착역이 같은 경로는 즐겨찾기에 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 경로는 즐겨찾기를 생성할 수 없다")
    void favoritePathNotValidTest() {
        final FavoriteRequest request = new FavoriteRequest(강남역.getId(), 서울역.getId());

        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, request))
                .isInstanceOf(FavoriteSaveException.class)
                .hasMessageContaining("존재하지 않는 경로는 즐겨찾기에 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("이미 등록한 즐겨찾기 경로는 다시 등록할 수 없다")
    void favoritePathDuplicateTest() {
        final FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());
        favoriteService.createFavorite(loginMember, request);

        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, request))
                .isInstanceOf(FavoriteSaveException.class)
                .hasMessageContaining("이미 등록된 즐겨찾기 경로입니다.");
    }

    @Test
    @DisplayName("즐겨찾기를 삭제할 수 있다")
    void deleteFavoriteTest() {
        final FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());
        final FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember, request);

        assertDoesNotThrow(() -> favoriteService.deleteFavorite(loginMember, favoriteResponse.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 즐겨찾기는 삭제할 수 없다")
    void deleteFavoriteNotExistTest() {
        final Long favoriteId = 1L;

        assertThatThrownBy(() -> favoriteService.deleteFavorite(loginMember, favoriteId))
                .isInstanceOf(FavoriteNotExistException.class);
    }
}
