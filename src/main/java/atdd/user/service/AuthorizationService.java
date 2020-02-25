package atdd.user.service;

import atdd.security.LoginUserRegistry;
import atdd.user.dto.AccessToken;
import atdd.security.LoginUserInfo;
import atdd.user.dto.TokenType;
import atdd.user.dto.UserResponseDto;
import io.jsonwebtoken.lang.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthorizationService {

    private static final Pattern WHITE_SPACE_SPLIT_PATTERN = Pattern.compile("\\s");

    private final UserService userService;
    private final JsonWebTokenProvider jsonWebTokenProvider;
    private final LoginUserRegistry loginUserRegistry;

    public AuthorizationService(UserService userService,
                                JsonWebTokenProvider jsonWebTokenProvider,
                                LoginUserRegistry loginUserRegistry) {

        this.userService = userService;
        this.jsonWebTokenProvider = jsonWebTokenProvider;
        this.loginUserRegistry = loginUserRegistry;
    }


    @Transactional(readOnly = true)
    public AccessToken authorize(String email, String password) {
        Assert.hasText(email, "email 은 필수입니다.");
        Assert.hasText(password, "password 는 필수입니다.");

        final UserResponseDto user = userService.findByEmail(email);
        if (isNotMatch(user, password)) {
            throw new IllegalArgumentException("password 가 일치하지 않습니다. password : [" + password + "]");
        }

        final Date nowDate = new Date();
        final String accessToken = jsonWebTokenProvider.createToken(email, nowDate);
        return AccessToken.ofBearerToken(accessToken);
    }

    private boolean isNotMatch(UserResponseDto user, String password) {
        return !Objects.equals(password, user.getPassword());
    }

    @Transactional(readOnly = true)
    public boolean isAuthorized(String authorization, Date nowDate) {
        if (!StringUtils.hasText(authorization) || Objects.isNull(nowDate)) {
            return false;
        }

        final String[] values = WHITE_SPACE_SPLIT_PATTERN.split(authorization);
        if (!isValidToken(values, nowDate)) {
            return false;
        }

        final String token = values[1];
        final String tokenEmail = jsonWebTokenProvider.parseEmail(token);
        final Optional<LoginUserInfo> loginUser = userService.findLoginUser(tokenEmail);
        if (!loginUser.isPresent()) {
            return false;
        }

        loginUserRegistry.setCurrentLoginUser(loginUser.get());
        return true;
    }

    private boolean isValidToken(String[] values, Date nowDate) {
        if (!isAuthorizationHeaderSize(values)) {
            return false;
        }

        if (!isBearerTokenType(values[0])) {
            return false;
        }

        return !jsonWebTokenProvider.isExpiredToken(values[1], nowDate);
    }

    private boolean isAuthorizationHeaderSize(String[] values) {
        return values.length == 2;
    }

    private boolean isBearerTokenType(String tokenType) {
        return TokenType.BEARER.isEqualTypeName(tokenType);
    }

}
