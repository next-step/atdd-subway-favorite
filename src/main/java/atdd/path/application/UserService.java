package atdd.path.application;

import atdd.path.application.dto.LoginRequestView;
import atdd.path.application.dto.LoginResponseView;
import atdd.path.application.dto.UserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.application.exception.FailedLoginException;
import atdd.path.application.exception.InvalidJwtAuthenticationException;
import atdd.path.domain.User;
import atdd.path.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
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

    public UserResponseView retrieveUser(HttpServletRequest request) {
        String email = this.extractEmail(request);

        User persistUser = userRepository.findUserByEmail(email);
        return UserResponseView.of(persistUser);
    }

    private String extractEmail(HttpServletRequest req) {
        String token = jwtTokenProvider.resolveToken(req);
        if (StringUtils.isEmpty(token) || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("invalid token");
        }
        return jwtTokenProvider.getUserEmail(token);
    }


    public LoginResponseView login(LoginRequestView loginRequestView) {
        User userInfo = userRepository.findUserByEmail(loginRequestView.getEmail());

        if (Objects.isNull(userInfo) || !userInfo.getPassword().equals(loginRequestView.getPassword())) {
            throw new FailedLoginException();
        }

        String token = jwtTokenProvider.createToken(loginRequestView.getEmail());
        return LoginResponseView.builder()
                .accessToken(token)
                .build();
    }
}
