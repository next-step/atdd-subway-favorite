package atdd.user.service;

import atdd.user.dto.AccessToken;
import atdd.user.dto.UserResponseDto;
import io.jsonwebtoken.lang.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class AuthorizationService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationService(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public AccessToken authorize(String email, String password) {
        Assert.hasText(email, "email 은 필수입니다.");
        Assert.hasText(password, "password 는 필수입니다.");

        final UserResponseDto user = userService.findByEmail(email);
        if (isNotMatch(user, password)) {
            throw new IllegalArgumentException("password 가 일치하지 않습니다. password : [" + password + "]");
        }

        final String accessToken = jwtTokenProvider.createToken(email);
        return AccessToken.ofBearerToken(accessToken);
    }

    private boolean isNotMatch(UserResponseDto user, String password) {
        return !Objects.equals(password, user.getPassword());
    }

}
