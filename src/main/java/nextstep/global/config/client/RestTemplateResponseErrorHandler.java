package nextstep.global.config.client;

import nextstep.global.error.exception.BusinessException;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().isError()) {
            byte[] body = getResponseBody(response);
            int rawStatusCode = response.getRawStatusCode();
            String errorMessage = getErrorMessage(rawStatusCode, response.getStatusText(), body, getCharset(response));
            throw new BusinessException(rawStatusCode, errorMessage);
        }
    }

    /**
     * Return error message with details from the response body. For example:
     * <pre>
     * 404 Not Found: [{'id': 123, 'message': 'my message'}]
     * </pre>
     */
    private String getErrorMessage(int rawStatusCode,
                                   String statusText,
                                   @Nullable byte[] responseBody,
                                   @Nullable Charset charset) {

        String preface = rawStatusCode + " " + statusText + ": ";

        if (ObjectUtils.isEmpty(responseBody)) {
            return preface + "[no body]";
        }

        charset = (charset != null ? charset : StandardCharsets.UTF_8);

        String bodyText = new String(responseBody, charset);
        bodyText = LogFormatUtils.formatValue(bodyText, -1, true);

        return preface + bodyText;
    }

}
