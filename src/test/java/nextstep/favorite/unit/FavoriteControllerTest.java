package nextstep.favorite.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.exception.NotExistFavoriteException;
import nextstep.member.application.JwtTokenProvider;
import nextstep.path.application.exception.NotAddedStationsToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
import nextstep.path.ui.exception.SameSourceAndTargetException;
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

import static nextstep.utils.GithubResponses.사용자1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("즐겨찾기 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
public class FavoriteControllerTest {

    private static final Long TEST_USER_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private FavoriteService favoriteService;

    private String token = "Bearer ";

    @BeforeEach
    void setUp() {
        token = String.format("Bearer %s", jwtTokenProvider.createToken(사용자1.getEmail(), TEST_USER_ID));
    }

    // TODO: 추후 3단계 리팩터링 과정에서 권한 관련 테스트 쪽으로 이동 해야 할 것 같습니다.
    @DisplayName("즐겨찾기 추가 함수는, 로그인하지 않은 경우 401 에러를 반환한다.")
    @Test
    void addFavoriteNotLoginTest() throws Exception {
        // given
        String jsonContent = 즐겨찾기에_추가할_경로("2");

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
        String jsonContent = 즐겨찾기에_추가할_경로("2");
        when(favoriteService.createFavorite(any(), any())).thenThrow(exceptionClass);

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
    @Test
    void addFavoritesTest() throws Exception {
        // given
        doThrow(SameSourceAndTargetException.class).when(favoriteService)
                .createFavorite(any(), any());
        String jsonContent = 즐겨찾기에_추가할_경로("1");

        // when & then
        mockMvc.perform(post("/favorites")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("즐겨찾기 삭제 함수는, 존재하지 않는 즐겨찾기를 삭제하려 하면 400 에러가 발생한다.")
    @Test
    void deleteFavoriteTest() throws Exception {
        // given
        doThrow(NotExistFavoriteException.class).when(favoriteService)
                .deleteFavorite(any(), any());

        // when & then
        mockMvc.perform(delete(String.format("/favorites/%s", 0))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    private String 즐겨찾기에_추가할_경로(String number) throws Exception {
        return mapToJson(Map.of("source", "1", "target", number));
    }
}
