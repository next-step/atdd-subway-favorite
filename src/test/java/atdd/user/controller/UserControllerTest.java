package atdd.user.controller;

import atdd.user.dto.UserCreateRequestDto;
import atdd.user.dto.UserResponseDto;
import atdd.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void create() throws Exception {
        final String email = "email@email.com";
        final String name = "name!!";
        final String password = "password!!";

        final UserCreateRequestDto requestDto = UserCreateRequestDto.of(email, name, password);

        final UserResponseDto expectedResponseDto = UserResponseDto.of(null, email, name, password);
        given(userService.create(requestDto)).willReturn(expectedResponseDto);

        final MockHttpServletResponse response = mockMvc.perform(post(UserController.ROOT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(toJson(requestDto)))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.이름").value(name))
                .andExpect(jsonPath("$.password").value(password))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        verify(userService, times(1)).create(requestDto);
    }

    private String toJson(UserCreateRequestDto requestDto) {
        try {
            return objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("json 변환중 오류발생 requestDto : " +  requestDto, e);
        }
    }

}