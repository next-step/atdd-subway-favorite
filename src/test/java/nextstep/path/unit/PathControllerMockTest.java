package nextstep.path.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class PathControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("출발역과 도착역이 같을 경우 400 에러를 응답한다.")
    @Test
    void sameSourceAndTargetException() throws Exception {
        // given
        String stationId = "1";

        // when & then
        mockMvc.perform(get("/paths")
                        .param("source", stationId)
                        .param("target", stationId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("출발역과 도착역은 달라야합니다."));
    }
}
