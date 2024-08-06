package nextstep.favorite.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("즐겨찾기 추가 함수는, 로그인하지 않은 경우 401 에러를 반환한다. **추후 3단계 리팩터링 과정에서 분리되어야 할 것 같습니다.**")
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
}
