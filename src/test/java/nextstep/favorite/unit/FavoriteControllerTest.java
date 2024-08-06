package nextstep.favorite.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.favorite.application.FavoriteService;
import nextstep.member.application.JwtTokenProvider;
import nextstep.path.application.exception.NotAddedStationsToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("즐겨찾기 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
public class FavoriteControllerTest {

    private static final String TEST_USER_EMAIL = "test@example.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private FavoriteService favoriteService;

    private String token = "Bearer ";

    @BeforeEach
    void setUp() {
        token = String.format("Bearer %s", jwtTokenProvider.createToken(TEST_USER_EMAIL));
    }

    // TODO: 추후 3단계 리팩터링 과정에서 권한 관련 테스트 쪽으로 이동 해야 할 것 같습니다.
    @DisplayName("즐겨찾기 추가 함수는, 로그인하지 않은 경우 401 에러를 반환한다.")
    @Test
    void addFavoriteNotLoginTest() throws Exception {
        // given
        Map<String, String> favoriteRequestMap = Map.of("source", "1", "target", "2");
        String jsonContent = mapToJson(favoriteRequestMap);

        // when & then
        mockMvc.perform(post("/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isUnauthorized());
    }

    private String mapToJson(Map<String, String> map) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
    }

    @DisplayName("즐겨찾기 추가 함수는, favoriteService.createFavorite()에서 에러가 발생하는 경우 400 에러를 응답한다.")
    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void addFavoritesServiceExceptionTest(Class<? extends Exception> exceptionClass) throws Exception {
        // given
        Map<String, String> favoriteRequestMap = Map.of("source", "1", "target", "2");
        String jsonContent = mapToJson(favoriteRequestMap);
        when(favoriteService.createFavorite(anyString(), any())).thenThrow(exceptionClass);

        // when & then
        mockMvc.perform(post("/favorites")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Class<? extends Exception>> exceptionProvider() {
        return Stream.of(NotAddedStationsToPathsException.class, NotConnectedPathsException.class);
    }

    @DisplayName("즐겨찾기 추가 함수는, 출발 역과 도착 역이 같은 경우 400 에러를 응답한다.")
    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void addFavoritesTest() throws Exception {
        // given
        Map<String, String> favoriteRequestMap = Map.of("source", "1", "target", "1");
        String jsonContent = mapToJson(favoriteRequestMap);

        // when & then
        mockMvc.perform(post("/favorites")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }
}
