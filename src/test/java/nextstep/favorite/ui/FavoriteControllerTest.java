package nextstep.favorite.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static nextstep.favorite.FavoriteUnitSteps.favoriteRequest;
import static nextstep.favorite.FavoriteUnitSteps.memberId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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

    @Test
    void 멤버추가() throws Exception {
        final FavoriteRequest favoriteRequest = favoriteRequest();

        doReturn(favoriteResponse())
                .when(favoriteService)
                .saveFavorite(any(), any(FavoriteRequest.class));

        final ResultActions result = mockMvc.perform(post("/favorites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(favoriteRequest)));

        result.andExpect(status().isCreated());
    }

    private FavoriteResponse favoriteResponse() {
        final FavoriteResponse favoriteResponse = new FavoriteResponse(memberId, null, null);
        ReflectionTestUtils.setField(favoriteResponse, "id", 1L);
        return favoriteResponse;
    }
}