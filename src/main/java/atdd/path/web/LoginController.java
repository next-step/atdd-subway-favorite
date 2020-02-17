package atdd.path.web;

import atdd.path.application.dto.LoginRequestView;
import atdd.path.application.dto.LoginResponseView;
import atdd.path.application.exception.UnauthorizedException;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RestController
public class LoginController {

    private final MemberDao memberDao;

    public LoginController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @PostMapping
    public ResponseEntity<LoginResponseView> login(@RequestBody LoginRequestView view) {
        final Member findMember = memberDao.findByEmail(view.getEmail()).orElseThrow(UnauthorizedException::new);

        if (!findMember.isMatchPassword(view.getPassword())) {
            throw new UnauthorizedException();
        }

        return ResponseEntity.ok(new LoginResponseView("bearer", ""));
    }

}
