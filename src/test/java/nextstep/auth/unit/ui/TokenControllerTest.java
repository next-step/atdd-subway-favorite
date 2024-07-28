package nextstep.auth.unit.ui;

import static nextstep.Fixtures.aMember;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.TokenService;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.ui.TokenController;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TokenController.class)
@Import({JwtTokenProvider.class})
@DisplayName("TokenController 단위 테스트")
class TokenControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private TokenService tokenService;

  @DisplayName("이메일과 비밀번호로 로그인한다.")
  @Test
  void loginWithEmailAndPassword() throws Exception {
    Member member = aMember().build();
    String accessToken = "xxxxx.yyyyy.zzzzz";
    given(tokenService.createToken(member.getEmail(), member.getPassword()))
        .willReturn(new TokenResponse(accessToken));

    mockMvc
        .perform(
            post("/login/token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(
                    "{\"email\": \""
                        + member.getEmail()
                        + "\", \"password\": \""
                        + member.getPassword()
                        + "\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(accessToken));
  }

  @DisplayName("Github 코드로 로그인한다.")
  @Test
  void loginWithGithubCode() throws Exception {
    String code = "authorization-code";
    String accessToken = "xxxxx.yyyyy.zzzzz";
    given(tokenService.createTokenFromGithubCode(code)).willReturn(new TokenResponse(accessToken));

    mockMvc
        .perform(
            post("/login/github")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"code\": \"" + code + "\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(accessToken));
  }
}
