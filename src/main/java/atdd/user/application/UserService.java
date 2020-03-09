package atdd.user.application;

import atdd.path.application.JwtTokenProvider;
import atdd.path.application.dto.LoginRequestView;
import atdd.path.application.dto.LoginResponseView;
import atdd.user.application.dto.UserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.application.exception.FailedLoginException;
import atdd.user.domain.User;
import atdd.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserResponseView createUser(UserRequestView userRequestView) {
        return UserResponseView.of(userRepository.save(userRequestView.toUser()));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserResponseView retrieveUser(String userEmail) {
        User persistUser = userRepository.findUserByEmail(userEmail);
        return UserResponseView.of(persistUser);
    }

    private String extractEmail(String requestToken) {
        if (StringUtils.isEmpty(requestToken) || !jwtTokenProvider.validateToken(requestToken)) {
            throw new FailedLoginException.InvalidJwtAuthenticationException("invalid token");
        }
        return jwtTokenProvider.getUserEmail(requestToken);
    }


    public LoginResponseView login(LoginRequestView loginRequestView) {
        User userInfo = userRepository.findUserByEmail(loginRequestView.getEmail());

        if (Objects.isNull(userInfo) || !userInfo.validatePassword(loginRequestView.getPassword())) {
            throw new FailedLoginException();
        }

        String token = jwtTokenProvider.createToken(loginRequestView.getEmail());
        return LoginResponseView.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }
}
