package atdd.user.controller;

import atdd.security.LoginUserInfo;
import atdd.security.LoginUserRegistry;
import atdd.user.dto.AccessToken;
import atdd.user.dto.TokenType;
import atdd.user.dto.UserCreateRequestDto;
import atdd.user.dto.UserResponseDto;
import atdd.user.service.AuthorizationService;
import atdd.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static atdd.user.controller.UserController.ROOT_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginUserRegistry loginUserRegistry;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorizationService authorizationService;


    @Test
    void create() throws Exception {
        final Long id = 451L;
        final String email = "email@email.com";
        final String name = "name!!";
        final String password = "password!!";

        final UserCreateRequestDto requestDto = UserCreateRequestDto.of(email, name, password);

        final UserResponseDto expectedResponseDto = UserResponseDto.of(id, email, name, password);
        given(userService.create(requestDto)).willReturn(expectedResponseDto);

        final MockHttpServletResponse response = mockMvc.perform(post(ROOT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(toJson(requestDto)))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.이름").value(name))
                .andExpect(jsonPath("$.password").value(password))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isEqualTo(ROOT_URI + "/" + id);

        verify(userService, times(1)).create(requestDto);
    }

    @Test
    void deleteUser() throws Exception {
        final Long id = 5487L;

        final MockHttpServletResponse response = mockMvc.perform(delete(ROOT_URI + "/" + id))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        verify(userService, times(1)).delete(id);
    }

    @Test
    void login() throws Exception {
        final String email = "email@email.com";
        final String password = "password!!";
        given(authorizationService.authorize(email, password)).willReturn(AccessToken.ofBearerToken("token!!!!"));


        mockMvc.perform(post(ROOT_URI + "/login")
                .queryParam("email", email)
                .queryParam("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value(TokenType.BEARER.getTypeName()));


        verify(authorizationService, times(1)).authorize(email, password);
    }

    @Test
    void getMyInfo() throws Exception {
        final Long userId = 14554L;
        final String email = "email@email.com";
        final String name = "name!!!";
        final String password = "password!!!";
        final String accessToken = "token!!!";
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        given(authorizationService.isAuthorized(any(), any())).willReturn(true);
        given(loginUserRegistry.getCurrentLoginUser()).willReturn(LoginUserInfo.of(userId, email, name, password));

        mockMvc.perform(get(ROOT_URI + "/me")
                .headers(httpHeaders))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.이름").value(name))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").value(password));
    }

    private String toJson(UserCreateRequestDto requestDto) {
        try {
            return objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("json 변환중 오류발생 requestDto : " + requestDto, e);
        }
    }

}