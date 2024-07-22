package nextstep.favorite.unit.ui;

import static nextstep.Fixtures.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.ui.FavoriteController;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FavoriteController.class)
@Import({JwtTokenProvider.class})
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("FavoriteController 단위 테스트")
class FavoriteControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  @MockBean
  private FavoriteService favoriteService;

  private String authorizationHeader;

  @BeforeEach
  void setUp() {
    Member member = aMember().build();
    String accessToken = jwtTokenProvider.createToken(member.getEmail());
    authorizationHeader = "Bearer " + accessToken;
  }

  @DisplayName("인증된 사용자가 즐겨찾기를 생성한다.")
  @Test
  void createFavorite() throws Exception {
    Station 교대역 = 교대역();
    Station 양재역 = 양재역();
    Favorite favorite = aFavorite().build();
    given(favoriteService.createFavorite(any(FavoriteRequest.class), any(LoginMember.class)))
        .willReturn(FavoriteResponse.of(favorite.getId(), 교대역, 양재역));

    mockMvc
        .perform(
            post("/favorites")
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"source\": " + 교대역.getId() + ", \"target\": " + 양재역.getId() + "}")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @DisplayName("인증되지 않은 사용자가 즐겨찾기를 생성하려고 하면 401 Unauthorized를 반환한다.")
  @Test
  void unauthenticatedCreateFavorite() throws Exception {
    mockMvc
        .perform(
            post("/favorites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"source\": 99, \"target\": 99}")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @DisplayName("인증된 사용자가 즐겨찾기 목록을 조회한다.")
  @Test
  void getFavorites() throws Exception {
    Station 교대역 = 교대역();
    Station 양재역 = 양재역();
    Station 강남역 = 강남역();
    Station 역삼역 = 역삼역();
    given(favoriteService.findFavorites())
        .willReturn(
            Arrays.asList(FavoriteResponse.of(1L, 교대역, 양재역), FavoriteResponse.of(2L, 강남역, 역삼역)));

    mockMvc
        .perform(get("/favorites").header(HttpHeaders.AUTHORIZATION, authorizationHeader))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].source.id").value(교대역.getId()))
        .andExpect(jsonPath("$[0].source.name").value(교대역.getName()))
        .andExpect(jsonPath("$[0].target.id").value(양재역.getId()))
        .andExpect(jsonPath("$[0].target.name").value(양재역.getName()))
        .andExpect(jsonPath("$[1].id").value(2L))
        .andExpect(jsonPath("$[1].source.id").value(강남역.getId()))
        .andExpect(jsonPath("$[1].source.name").value(강남역.getName()))
        .andExpect(jsonPath("$[1].target.id").value(역삼역.getId()))
        .andExpect(jsonPath("$[1].target.name").value(역삼역.getName()));
  }

  @DisplayName("인증되지 않은 사용자가 즐겨찾기 목록을 조회하려고 하면 401 Unauthorized를 반환한다.")
  @Test
  void unauthorizedGetFavorites() throws Exception {
    mockMvc
        .perform(get("/favorites"))
        .andExpect(status().isUnauthorized());
  }
}
