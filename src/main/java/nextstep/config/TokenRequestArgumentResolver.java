package nextstep.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.TokenRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class TokenRequestArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String JSON_BODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(TokenRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String requestBody = getRequestBody(webRequest);

        TokenRequest tokenRequest = objectMapper.readValue(requestBody, TokenRequest.class);

        checkTokenRequest(tokenRequest);

        return tokenRequest;
    }

    private String getRequestBody(NativeWebRequest webRequest) throws IOException {
        HttpServletRequest httpServletRequest = Objects.requireNonNull(
            webRequest.getNativeRequest(HttpServletRequest.class));
        String jsonBody = (String) httpServletRequest.getAttribute(JSON_BODY_ATTRIBUTE);

        if (jsonBody == null) {
            jsonBody = IOUtils.toString(httpServletRequest.getInputStream(), Charsets.UTF_8);
            httpServletRequest.setAttribute(JSON_BODY_ATTRIBUTE, jsonBody);
        }

        return jsonBody;
    }

    private void checkTokenRequest(TokenRequest tokenRequest) {
        Preconditions.checkArgument(tokenRequest.getEmail() != null);
        Preconditions.checkArgument(tokenRequest.getPassword() != null);
    }
}
