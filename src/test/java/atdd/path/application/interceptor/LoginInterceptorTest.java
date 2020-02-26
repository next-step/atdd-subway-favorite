package atdd.path.application.interceptor;

import atdd.path.application.provider.JwtTokenProvider;
import atdd.path.dao.MemberDao;
import atdd.path.web.MemberController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static atdd.path.TestConstant.TEST_MEMBER;
import static atdd.path.TestConstant.TEST_MEMBER_EMAIL;
import static atdd.path.application.provider.JwtTokenProvider.TOKEN_TYPE;
import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JwtTokenProvider.class})
@WebMvcTest(MemberController.class)
class LoginInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberDao memberDao;

    @DisplayName("LoginInterceptor 를 통과 할 수 있다")
    @Test
    void beAbleToPassLoginInterceptor() throws Exception {
        given(memberDao.findByEmail(TEST_MEMBER_EMAIL)).willReturn(of(TEST_MEMBER));

        String token = jwtTokenProvider.createToken(TEST_MEMBER_EMAIL);

        mockMvc.perform(get("/members/me")
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TEST_MEMBER_EMAIL));
    }

    @DisplayName("LoginInterceptor 를 통과 할 수 없다")
    @Test
    void beAbleToBlockLoginInterceptor() throws Exception {
        String token = jwtTokenProvider.createToken(TEST_MEMBER_EMAIL);

        mockMvc.perform(get("/members/me")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}