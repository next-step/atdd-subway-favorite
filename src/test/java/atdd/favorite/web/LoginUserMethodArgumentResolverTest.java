package atdd.favorite.web;

import atdd.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
public class LoginUserMethodArgumentResolverTest {
    public static final String FAVORITE_STATION_BASE_URI = "/favorite-station";
    public static final String NAME = "브라운";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HttpServletRequest httpServletRequest;

    @Test
    void 유효한_토큰이_주어지면_회원정보를_리턴한다() throws Exception {
        //given
        User user = new User(EMAIL, NAME, PASSWORD);
        given(httpServletRequest.getAttribute("email")).willReturn(EMAIL);

        //when
        mockMvc.perform(post(URI.create(FAVORITE_STATION_BASE_URI))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getRequest().getAttribute("user").equals(user);
    }
}
