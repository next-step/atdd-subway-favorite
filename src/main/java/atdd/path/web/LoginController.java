package atdd.path.web;

import atdd.path.application.JwtTokenProvider;
import atdd.path.application.dto.CreateMemberRequestView;
import atdd.path.application.dto.TokenResponseView;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController
{
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public LoginController(MemberDao memberDao, JwtTokenProvider jwtTokenProvider)
    {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @RequestMapping
    public ResponseEntity<TokenResponseView> login(@RequestBody CreateMemberRequestView view)
    {
        Member member = memberDao.findByEmailAndPassword(view.getEmail(), view.getPassword());
        String accessToken = jwtTokenProvider.createToken(member.getEmail());
        return ResponseEntity.ok().body(TokenResponseView.of(accessToken));
    }

}
