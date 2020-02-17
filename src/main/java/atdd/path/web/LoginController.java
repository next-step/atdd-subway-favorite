package atdd.path.web;

import atdd.path.application.dto.LoginRequestView;
import atdd.path.application.dto.LoginResponseView;
import atdd.path.application.exception.UnauthorizedException;
import atdd.path.application.provider.JwtTokenProvider;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static atdd.path.application.provider.JwtTokenProvider.TOKEN_TYPE;

@RequestMapping("/login")
@RestController
public class LoginController {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<LoginResponseView> login(@RequestBody LoginRequestView view) {
        final String email = view.getEmail();
        final Member findMember = memberDao.findByEmail(email).orElseThrow(UnauthorizedException::new);

        if (!findMember.isMatchPassword(view.getPassword())) {
            throw new UnauthorizedException();
        }

        final String accessToken = jwtTokenProvider.createToken(email);
        return ResponseEntity.ok(new LoginResponseView(TOKEN_TYPE, accessToken));
    }

}
