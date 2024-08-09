package nextstep.auth.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.OauthFacadeService;
import nextstep.auth.application.dto.ApplicationTokenResponse;
import nextstep.auth.application.dto.OauthTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthFacadeService oauthFacadeService;

    @PostMapping("/github")
    public ResponseEntity<ApplicationTokenResponse> getAccessToken(@RequestBody OauthTokenRequest request) {
        ApplicationTokenResponse applicationTokenResponse = oauthFacadeService.getApplicationToken(request.getCode());
        return ResponseEntity.ok(applicationTokenResponse);
    }
}
