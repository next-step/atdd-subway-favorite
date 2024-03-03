package nextstep.member.ui;

import nextstep.member.application.OAuth2Service;
import nextstep.member.application.dto.OAuth2Request;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    public OAuth2Controller(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> login(@ModelAttribute OAuth2Request oAuth2Request) {
        oAuth2Service.login(oAuth2Request);
        return ResponseEntity.ok().build();
    }

}
