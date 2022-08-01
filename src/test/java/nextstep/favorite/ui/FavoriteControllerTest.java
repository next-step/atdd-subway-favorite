package nextstep.favorite.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static nextstep.favorite.FavoriteUnitSteps.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        doReturn(email)
                .when(jwtTokenProvider)
                .getPrincipal(anyString());

        doReturn(List.of())
                .when(jwtTokenProvider)
                .getRoles(anyString());

        doReturn(true)
                .when(jwtTokenProvider)
                .validateToken(anyString());
    }

    @Test
    void 즐겨찾기추가() throws Exception {
        final FavoriteRequest favoriteRequest = favoriteRequest();

        doReturn(favoriteResponse())
                .when(favoriteService)
                .saveFavorite(anyString(), any(FavoriteRequest.class));

        final ResultActions result = mockMvc.perform(post("/favorites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(favoriteRequest)));

        result.andExpect(status().isCreated());
    }

    @Test
    void 즐겨찾기목록조회() throws Exception {
        doReturn(List.of(favoriteResponse()))
                .when(favoriteService)
                .findFavorites(anyString());

        final ResultActions result = mockMvc.perform(get("/favorites"));

        result.andExpect(status().isOk());
    }

    @Test
    void 즐겨찾기조회() throws Exception {
        doReturn(favoriteResponse())
                .when(favoriteService)
                .findFavorite(anyString(), anyLong());

        final ResultActions result = mockMvc.perform(get("/favorites/{id}", 1L));

        result.andExpect(status().isOk());
    }

    private FavoriteResponse favoriteResponse() {
        final FavoriteResponse favoriteResponse = new FavoriteResponse(memberId, null, null);
        ReflectionTestUtils.setField(favoriteResponse, "id", 1L);
        return favoriteResponse;
    }
}