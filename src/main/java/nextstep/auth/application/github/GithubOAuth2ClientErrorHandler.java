package nextstep.auth.application.github;

import nextstep.common.exception.BadRequestException;
import nextstep.common.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class GithubOAuth2ClientErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(final ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is5xxServerError() ||
                httpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(final ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            throw new UnauthorizedException("인증에 실패했습니다");
        }
        throw new BadRequestException("잘못된 요청입니다.");
    }
}
