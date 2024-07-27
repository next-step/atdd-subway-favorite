package nextstep.subway.unit.path.ui;

import static nextstep.Fixtures.교대역;
import static nextstep.Fixtures.양재역;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.application.dto.PathRequest;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.ui.PathController;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PathController.class)
@DisplayName("경로 조회 컨트롤러 단위 테스트")
@SuppressWarnings("NonAsciiCharacters")
class PathControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private PathService pathService;
  @MockBean private JwtTokenProvider jwtTokenProvider;

  @Test
  @DisplayName("경로를 조회 요청에 응답한다.")
  void findPath() throws Exception {
    Station 교대역 = 교대역();
    Station 양재역 = 양재역();
    PathRequest request = PathRequest.of(교대역.getId(), 양재역.getId());
    given(pathService.findPath(request)).willReturn(Path.of(List.of(교대역, 양재역), 5));

    mockMvc
        .perform(
            get("/paths")
                .param("source", String.valueOf(교대역.getId()))
                .param("target", String.valueOf(양재역.getId())))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stations", hasSize(2)))
        .andExpect(jsonPath("$.stations[0].id").value(교대역.getId()))
        .andExpect(jsonPath("$.stations[0].name").value(교대역.getName()))
        .andExpect(jsonPath("$.stations[1].id").value(양재역.getId()))
        .andExpect(jsonPath("$.stations[1].name").value(양재역.getName()))
        .andExpect(jsonPath("$.distance").value(5));
  }
}
