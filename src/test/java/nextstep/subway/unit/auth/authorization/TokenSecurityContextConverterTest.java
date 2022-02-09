package nextstep.subway.unit.auth.authorization;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.authorization.converter.TokenSecurityContextConverter;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.token.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class TokenSecurityContextConverterTest {
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private TokenSecurityContextConverter tokenSecurityContextConverter;

    @Test
    void convert() throws JsonProcessingException {
        // given
        Map<String, String> PAYLOAD = new HashMap<>();
        PAYLOAD.put("HELLO", "안녕");

        when(jwtTokenProvider.validateToken(any()))
            .thenReturn(true);
        when(jwtTokenProvider.getPayload(any()))
            .thenReturn(new ObjectMapper().writeValueAsString(PAYLOAD));

        // when
        SecurityContext securityContext = tokenSecurityContextConverter.convert(new MockHttpServletRequest());

        // then
        assertThat(securityContext.getAuthentication().getPrincipal())
            .isInstanceOf(Map.class);
    }
}
