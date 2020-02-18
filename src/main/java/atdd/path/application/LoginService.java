package atdd.path.application;

import atdd.path.application.dto.LoginRequestView;
import atdd.path.application.dto.LoginResponseView;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public final JwtTokenProvider jwtTokenProvider;

    public LoginService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseView login(LoginRequestView loginRequestView) {
        String token = jwtTokenProvider.createToken(loginRequestView.getEmail());

        return LoginResponseView.builder()
                .accessToken(token)
                .build();
    }
}
