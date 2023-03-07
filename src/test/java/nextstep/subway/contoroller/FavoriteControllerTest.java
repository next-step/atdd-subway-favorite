package nextstep.subway.contoroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.ui.FavoriteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HttpServletRequest httpServletRequest;
    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private FavoriteService favoriteService;
    private ObjectMapper mapper;

    @BeforeEach
    void setUP() {
        mapper = new ObjectMapper();
    }

    @DisplayName("인증 정보 없이 즐겨찾기 추가")
    @Test
    public void addFavoriteWithoutAuthentication() throws Exception {
        when(httpServletRequest.getAttribute("user")).thenReturn(null);
        final FavoriteRequest favoriteRequest = new FavoriteRequest("1", "2");
        this.mockMvc.perform(post("/favorites")
                        .content(mapper.writeValueAsString(favoriteRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("인증 정보 없이 즐겨찾기 목록 조회")
    @Test
    void showFavoritesWithoutAuthentication() throws Exception {
        when(httpServletRequest.getAttribute("user")).thenReturn(null);
        this.mockMvc.perform(get("/favorites"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("인증 정보 없이 즐겨찾기 삭제")
    @Test
    void deleteFavoriteWithoutAuthentication() throws Exception {
        when(httpServletRequest.getAttribute("user")).thenReturn(null);
        this.mockMvc.perform(delete("/favorites/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
