package nextstep.subway.auth.application;

import nextstep.subway.auth.application.dto.UserTokenRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTokenServiceProvider {
    private List<UserTokenService> userTokenServices;

    public UserTokenServiceProvider(List<UserTokenService> userTokenServices) {
        this.userTokenServices = userTokenServices;
    }

    public String createToken(UserTokenRequest userTokenRequest) {
        for (UserTokenService userTokenService : userTokenServices) {
            String token = userTokenService.createToken(userTokenRequest);
            if (token != null) {
                return token;
            }
        }
        throw new IllegalArgumentException("요청에 적합한 UserTokenService가 없습니다.");
    }
}
