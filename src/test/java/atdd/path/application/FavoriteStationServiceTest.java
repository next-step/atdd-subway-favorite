package atdd.path.application;

import atdd.path.TestConstant;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.User;
import atdd.path.repository.FavoriteStationRepository;
import atdd.path.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {FavoriteService.class, JwtTokenProvider.class})
@TestPropertySource(properties = {
        "security.jwt.secret-key=secretsecretsecretsecretsecr",
        "security.jwt.expire-length=3600000"
})
public class FavoriteStationServiceTest {

    private FavoriteService favoriteService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FavoriteStationRepository favoriteStationRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.favoriteService = new FavoriteService(jwtTokenProvider, userRepository, favoriteStationRepository);
    }

    @DisplayName("지하철 역 즐겨찾기 추가")
    @Test
    void createFavoriteStation() {
        // give
        given(userRepository.findUserByEmail(any(String.class)))
                .willReturn(User.createBuilder().id(1L).email(TestConstant.EMAIL_BROWN).build());
        given(favoriteStationRepository.save(any(FavoriteStation.class)))
                .willReturn(FavoriteStation.builder().userId(1L).stationId(1L).build());
        String token = jwtTokenProvider.createToken(TestConstant.EMAIL_BROWN);

        // when
        FavoriteResponseView response = favoriteService.createStationFavorite(String.format("Bearer %s", token), 1L);

        // then
        assertThat(response.getStationId()).isNotNull();
    }

}
