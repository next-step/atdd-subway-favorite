package nextstep.subway.ui;


import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.created(URI.create("/favorites"))
                .build();
    }

    @GetMapping
    public ResponseEntity<FavoriteResponse> findFavorite(@AuthenticationPrincipal LoginMember loginMember) {
        FavoriteResponse response = null;
        return ResponseEntity.ok(response);
    }

}
