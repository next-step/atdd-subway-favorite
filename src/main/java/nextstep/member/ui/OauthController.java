package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.OauthService;
import nextstep.member.application.dto.AccessTokenRequest;
import nextstep.member.application.dto.AccessTokenResponse;
import nextstep.member.application.dto.ResourceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @PostMapping("/github")
    public ResponseEntity<AccessTokenResponse> getAccessToken(@RequestBody AccessTokenRequest request) {
        ResourceResponse resourceResponse = oauthService.authenticate(request.getCode());


        return null;
    }

}
