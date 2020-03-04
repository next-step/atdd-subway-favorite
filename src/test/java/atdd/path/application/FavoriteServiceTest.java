package atdd.path.application;

import atdd.path.TestConstant;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.application.dto.FavoriteRouteResponseView;
import atdd.path.domain.FavoriteRoute;
import atdd.path.domain.FavoriteStation;
import atdd.path.repository.FavoriteRouteRepository;
import atdd.path.repository.FavoriteStationRepository;
import atdd.user.domain.User;
import atdd.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {FavoriteService.class, JwtTokenProvider.class})
@TestPropertySource(properties = {
        "security.jwt.secret-key=secretsecretsecretsecretsecr",
        "security.jwt.expire-length=3600000"
})
public class FavoriteServiceTest {

    private FavoriteService favoriteService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FavoriteStationRepository favoriteStationRepository;

    @MockBean
    private FavoriteRouteRepository favoriteRouteRepository;

    @BeforeEach
    void setUp() {
        this.favoriteService = new FavoriteService(favoriteStationRepository, favoriteRouteRepository);
    }

    @DisplayName("지하철 역 즐겨찾기 추가")
    @Test
    void createFavoriteStation() {
        // give
        given(userRepository.findUserByEmail(any(String.class)))
                .willReturn(User.createBuilder().id(1L).email(TestConstant.EMAIL_BROWN).build());
        given(favoriteStationRepository.save(any(FavoriteStation.class)))
                .willReturn(FavoriteStation.builder().userId(1L).stationId(1L).build());
        User user = userRepository.findUserByEmail(TestConstant.EMAIL_BROWN);

        // when
        FavoriteResponseView response = favoriteService.createStationFavorite(1L, user);

        // then
        assertThat(response.getStation()).isNotNull();
    }

    @DisplayName("지하철 역 즐겨찾기 조회")
    @Test
    void findAllFavoriteStation() {
        // given
        given(userRepository.findUserByEmail(any(String.class)))
                .willReturn(User.createBuilder().id(1L).email(TestConstant.EMAIL_BROWN).build());
        given(favoriteStationRepository.findAllByUserId(anyLong()))
                .willReturn(Collections.singletonList(FavoriteStation.builder().id(1L).userId(1L).stationId(1L).build()));
        User user = userRepository.findUserByEmail(TestConstant.EMAIL_BROWN);

        // when
        List<FavoriteResponseView> favorites = favoriteService.findFavoriteStation(user);

        // then
        assertThat(favorites.size()).isEqualTo(1);
    }

    @DisplayName("지하철 역 즐겨찾기 삭제")
    @Test
    void deleteFavoriteStation() {
        // give
        given(userRepository.findUserByEmail(any(String.class)))
                .willReturn(User.createBuilder().id(1L).email(TestConstant.EMAIL_BROWN).build());
        User user = userRepository.findUserByEmail(TestConstant.EMAIL_BROWN);

        // when
        favoriteService.deleteFavoriteStation(user, 1L);

        // then
        verify(favoriteStationRepository).deleteByIdAndUserId(anyLong(), anyLong());
    }

    @DisplayName("경로 즐겨찾기 등록")
    @Test
    void createFavoriteRoute() {
        // given
        given(userRepository.findUserByEmail(any(String.class)))
                .willReturn(User.createBuilder().id(1L).email(TestConstant.EMAIL_BROWN).build());
        User user = userRepository.findUserByEmail(TestConstant.EMAIL_BROWN);
        given(favoriteRouteRepository.save(any(FavoriteRoute.class)))
                .willReturn(FavoriteRoute.builder().userId(user.getId()).sourceStationId(1L).targetStationId(2L).build());

        // when
        FavoriteRouteResponseView response = favoriteService.createRouteFavorite(1L, 2L, user);

        // then
        assertThat(response.getSourceStation()).isNotNull();
        assertThat(response.getTargetStation()).isNotNull();
    }

    @DisplayName("경로 즐겨찾기 조회")
    @Test
    void findFavoriteRoute() {
        // given
        given(userRepository.findUserByEmail(any(String.class)))
                .willReturn(User.createBuilder().id(1L).email(TestConstant.EMAIL_BROWN).build());
        User user = userRepository.findUserByEmail(TestConstant.EMAIL_BROWN);
        given(favoriteRouteRepository.save(any(FavoriteRoute.class)))
                .willReturn(FavoriteRoute.builder().userId(user.getId()).sourceStationId(1L).targetStationId(2L).build());
        given(favoriteRouteRepository.findAllByUserId(user.getId()))
                .willReturn(Collections.singletonList(FavoriteRoute.builder().id(1L).userId(user.getId()).sourceStationId(1L).targetStationId(2L).build()));
        favoriteService.createRouteFavorite(1L, 2L, user);

        // when
        List<FavoriteRouteResponseView> response = favoriteService.findFavoriteRoute(user);

        // then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getTargetStation()).isNotNull();
        assertThat(response.get(0).getSourceStation()).isNotNull();
    }
}
