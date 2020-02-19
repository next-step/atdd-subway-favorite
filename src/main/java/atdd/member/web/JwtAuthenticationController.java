package atdd.member.web;

import atdd.member.application.dto.JwtTokenResponseView;
import atdd.member.application.dto.LoginMemberRequestView;
import atdd.member.dao.MemberDao;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class JwtAuthenticationController {

    @PostMapping
    public ResponseEntity<JwtTokenResponseView> login(@Valid @RequestBody LoginMemberRequestView view) {
        return ResponseEntity.ok(new JwtTokenResponseView("token"));
    }
}
